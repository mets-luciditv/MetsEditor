package metseditor

import grails.gorm.transactions.Transactional
import groovy.xml.MarkupBuilder

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import java.util.regex.Matcher

@Transactional
class TeiP5Service {

    def assetResourceLocator
    def xsltTransformer(String input,String xslt) {
        def sw=new StringWriter()
        def transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new StringReader(xslt)))
        transformer.transform(new StreamSource(new StringReader(input)),new StreamResult(sw))
        return sw.toString()
    }
    def replacePageNoAndLineNumber(String input,String pageNo){
        def p = ~/(P0000L00)/
        def pageStr =("0000"+pageNo).substring(pageNo.length());
        int lineNumber=1
        input.replaceAll(p){ all, page ->
            def lineNumberStr =String.format("%02d",lineNumber++)
            "P${pageStr}L${lineNumberStr}"
        }

    }
    def createXmlByBookId(long bookId) {

        def book=Book.get(bookId)
        def xmlWriter = new StringWriter()
        def xmlMarkup = new MarkupBuilder(xmlWriter)
        xmlMarkup.doubleQuotes=true

        xmlMarkup.'TEI'('xmlns':"http://www.tei-c.org/ns/1.0",'xml:id':book.classCode){
            teiHeader(){
                fileDesc(){
                    titleStmt(){
                        title(book.title)
                        author(){
                            book.author.split(",").each {author ->
                                persName(author)
                            }
                        }
                    }
                }
            }
            def tasks=Task.findAllByBook(book,[sort:'pageNum',order:'asc'])
            def sb= new StringBuilder()
            tasks.each {task ->
                def xslt =  assetResourceLocator
            }

            text(sb.toString()){


            }
        }
        return  xmlWriter.toString()
    }
}
