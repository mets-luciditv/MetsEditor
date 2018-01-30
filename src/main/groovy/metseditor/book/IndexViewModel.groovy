package metseditor.book

import groovy.xml.MarkupBuilder
import metseditor.TeiP5Service
import org.zkoss.bind.BindContext
import org.zkoss.bind.BindUtils
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.ContextParam
import org.zkoss.bind.annotation.ContextType
import org.zkoss.bind.annotation.Init
import org.zkoss.spring.DelegatingVariableResolver
import org.zkoss.util.media.Media
import org.zkoss.util.resource.Labels
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.event.UploadEvent
import org.zkoss.zk.ui.select.annotation.VariableResolver
import org.zkoss.zk.ui.select.annotation.WireVariable
import org.zkoss.zul.ListModelList

import metseditor.Book
import org.zkoss.zul.Messagebox

import java.text.MessageFormat

@VariableResolver(DelegatingVariableResolver.class)
class IndexViewModel {

    ListModelList<BookRow> bookRows=new ListModelList<BookRow>()
    @WireVariable springSecurityService
    @WireVariable TeiP5Service teiP5Service
    @WireVariable def grailsApplication
    @Init
    def init() {
        for(book in Book.findAll()){
            bookRows.add(new BookRow(book:book,isNew: false))
        }
    }
    @Command
    def doUploadPhoto(@ContextParam(ContextType.BIND_CONTEXT) BindContext ctx, @BindingParam('row') BookRow row) {
        UploadEvent event = (UploadEvent)ctx.getTriggerEvent()
        def pageImageRoot=grailsApplication.config.getProperty("app.pageImageRoot")
        for(media in event.medias)
        {
            def outputStream = new FileOutputStream(new File("${pageImageRoot}/${row.book.classCode}/${media.name}"))
            def inputStream = media.getStreamData()
            byte[] buffer = new byte[1024]
            for (int count; (count = inputStream.read(buffer)) != -1;) {
                outputStream.write(buffer, 0, count)
            }
            outputStream.flush()
            outputStream.close()
            inputStream.close()
        }


    }
    @Command
    def edit(@BindingParam('row') BookRow row){
        row.inEditing = ! row.inEditing
        bookRows.set(bookRows.indexOf(row),row)
    }
    @Command
    def save(@BindingParam('row') BookRow row){
        if(row.isNew) {
            def pageImageRoot=grailsApplication.config.getProperty("app.pageImageRoot")
            def folder=new File("${pageImageRoot}/${row.book.classCode}")
            if(!folder.exists()){
                folder.mkdir()
            }

        }else {

            def book=Book.get(row.book.id)
            book.title=row.book.title
            book.author=row.book.author
            book.classCode=row.book.classCode
            book.editor=row.book.editor
            book.publisher=row.book.publisher
            row.book=book
        }
        if(row.book.save(flush:true)){
            row.isNew=false
            row.inEditing = ! row.inEditing
            bookRows.set(bookRows.indexOf(row),row)
        }
        else{

            Messagebox.show(String.join("\n",row.book.errors.getAllErrors().collect {MessageFormat.format(it.defaultMessage,it.arguments)   }))
        }

    }
    @Command
    def cancel(@BindingParam('row') BookRow row){
        row.inEditing = ! row.inEditing
        row.book=Book.get(row.book.id)
        bookRows.set(bookRows.indexOf(row),row)
    }
    @Command
    def newRow(){
        bookRows.add(new BookRow(book: new Book(),inEditing: true,isNew: true))

    }
    @Command
    def delete(@BindingParam('row') BookRow row){
        Messagebox.show( Labels.getLabel("confirm_message_delete"),Labels.getLabel("alert"),Messagebox.YES|Messagebox.NO, Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener(){
                    public void onEvent(Event evt) throws InterruptedException {
                        if (evt.getName().equals("onYes")) {
                            if (bookRows.contains(row))
                            {
                                bookRows.remove(row)
                            }
                            Book.get(row.book.id).delete(flush:true)
                        }
                        else{

                        }
                        //BindUtils.postNotifyChange(null, null, this, "*");
                }})
    }
    @Command
    def teip5(@BindingParam('row') BookRow row){
        teiP5Service.createXmlByBookId(row.book.id)
    }
    @Command
    def open(@BindingParam('row') BookRow row){
        Executions.getCurrent().sendRedirect("./edit.zul?book=${row.book.id}","_blank")
    }
}
