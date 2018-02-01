package metseditor.book

import metseditor.Book
import metseditor.Task
import metseditor.typing.TaskRow
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.spring.DelegatingVariableResolver
import org.zkoss.util.resource.Labels
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.VariableResolver
import org.zkoss.zk.ui.select.annotation.WireVariable
import org.zkoss.zk.ui.util.Clients

@VariableResolver(DelegatingVariableResolver.class)
class EditViewModel {
    @WireVariable springSecurityService
    @WireVariable solrService
    Book book
    @Init
    def init(){
        def bookId=Long.parseLong(Executions.getCurrent().getParameter("book"))
        book = Book.get(bookId)
        Executions.current.desktop.getFirstPage().setTitle("${book.title} ${book.volume_title}")
    }

    @Command
    def save(){
        def _book=Book.get(book.id)
        _book.content=book.content
        _book.save(flush:true)
    }
    @Command
    def publish(){
        def _book=Book.get(book.id)
        _book.content=book.content
        _book.save(flush:true)
        if(solrService.publish(book)==0)
        {
            Clients.showNotification(Labels.getLabel("publishOK"))
        }
        else{
            Clients.showNotification(Labels.getLabel("publishNG"))
        }
    }
}
