package metseditor

import grails.gorm.transactions.Transactional
import org.apache.solr.client.solrj.SolrClient
import org.apache.solr.client.solrj.impl.HttpSolrClient
import org.apache.solr.common.SolrInputDocument

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@Transactional
class SolrService {
    def grailsApplication
    int publish(Task task) {
        def urlString = grailsApplication.config.getProperty("app.solrUrl")
        SolrClient solr = new HttpSolrClient.Builder(urlString).build()
        def pageStr =("0000"+task.pageNum).substring(task.pageNum.length())
        String html="""<span class="pb" data-pageid="P${pageStr}"><img src="PageImage/${task.book.classCode}/${task.pageImageName}.png" /></span>${task.content}"""
        //html=addLineNumberLabel(html)
        //html=replacePageNoAndLineNumber(html,task.pageNum)
        //html=addBrClass(html)
        def document = new SolrInputDocument()

        document.addField("jing_id", task.book.classCode)
        document.addField("jing_title",task.book.title)
        document.addField("author",task.book.author.split(","))
        document.addField("html",html)
        //document.addField("lineids","")
        document.addField("text",extractText("<div>"+html+"</div>"))
        document.addField("volume_id","")
        document.addField("volume_title","")

        document.addField("id","${task.book.classCode}P${pageStr}")
        def response = solr.add(document)
        if(response.status==0){
            return solr.commit().status
        }
        return response.status

    }
    def addLineNumberLabel(String html)
    {
        String xslt="""<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" >
    <xsl:output method="xml" omit-xml-declaration="yes" indent="no"/>
    <xsl:template match="p">
        <xsl:copy>
                <xsl:copy-of select="@*"/>
                <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    <xsl:template match="p/node()">
        <xsl:call-template name="IdentityTransform" />
    </xsl:template>
    <xsl:template match="p/child::node()[1]">
        <span class="lb" data-lineid="P0000L00">&amp;nbsp;</span><xsl:call-template name="IdentityTransform" />
    </xsl:template>
    <xsl:template match="p/br">
        <br></br> <span class="lb" data-lineid="P0000L00">&amp;nbsp;</span>
    </xsl:template>
    <xsl:template match="@*|node()">
        <xsl:call-template name="IdentityTransform" />
    </xsl:template>
    <xsl:template name="IdentityTransform">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="p/text()">
        <xsl:if test="position() = 1"><span class="lb" data-lineid="P0000L00">&amp;nbsp;</span></xsl:if>
     <xsl:value-of 
            select="translate(.,'&#xA;','')"/>
    </xsl:template>
</xsl:stylesheet>"""
        def sw=new StringWriter()
        def transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new StringReader(xslt)))
        transformer.transform(new StreamSource(new StringReader(html.replaceAll("&nbsp;","&amp;nbsp;"))),new StreamResult(sw))
        return sw.toString().replaceAll("&amp;nbsp;","&nbsp;")
    }
    def replacePageNoAndLineNumber(String input,String pageNo){
        def p = ~/(P0000L00)/
        def pageStr =("0000"+pageNo).substring(pageNo.length())
        int lineNumber=1
        input.replaceAll(p){ all, page ->
            def lineNumberStr =("00"+lineNumber++).substring(2)
            "P${pageStr}L${lineNumberStr}"
        }

    }
    def addBrClass(String html){ //對br加上class讓網頁可以切換「原書換行」
        return html.replaceAll("<br/>|<br>",'<br class="lb_br"/>')
    }
    def extractText(String html) {
        String xslt="""<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl='http://www.w3.org/1999/XSL/Transform' xmlns:tei='http://www.tei-c.org/ns/1.0' version='1.0' exclude-result-prefixes="tei">
    <xsl:output method="html" indent="yes" encoding="UTF-8"  />
</xsl:stylesheet>"""
        def sw=new StringWriter()
        def transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(new StringReader(xslt)))
        transformer.transform(new StreamSource(new StringReader(html.replaceAll("&nbsp;","&amp;nbsp;"))),new StreamResult(sw))
        return sw.toString().replaceAll("&amp;nbsp;","&nbsp;")
    }
}
