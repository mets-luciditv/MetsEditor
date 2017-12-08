package metseditor.task

import grails.plugin.springsecurity.SpringSecurityUtils
import metseditor.*
import metseditor.typing.TaskRow
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.filefilter.WildcardFileFilter
import org.jsoup.Jsoup
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.bind.annotation.NotifyChange
import org.zkoss.spring.DelegatingVariableResolver
import org.zkoss.util.resource.Labels
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.select.annotation.VariableResolver
import org.zkoss.zk.ui.select.annotation.WireVariable
import org.zkoss.zk.ui.util.Clients
import org.zkoss.zul.Filedownload
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

import javax.imageio.ImageIO
import java.text.MessageFormat

@VariableResolver(DelegatingVariableResolver.class)
class IndexViewModel {
    @WireVariable springSecurityService
    @WireVariable solrService
    ListModelList<TaskRow> taskRows = new ListModelList<TaskRow>()
    ListModelList<Book> books = new ListModelList<Book>()
    ListModelList<User> typists = new ListModelList<User>()
    ListModelList<User> proofreaders1 = new ListModelList<User>()
    ListModelList<User> proofreaders2 = new ListModelList<User>()
    ListModelList<User> proofreaders3 = new ListModelList<User>()
    List<String> pages=new ArrayList<String>()
    List<String> fromPages= new ArrayList<String>()
    List<String> toPages= new ArrayList<String>()
    @WireVariable def grailsApplication

    Book batchBook
    String batchClassCode
    String batchFromPage
    String batchToPage
    User batchTypist
    User batchProofreader1
    User batchProofreader2
    User batchProofreader3

    String pageImageRoot
    @Init
    def init() {
        pageImageRoot=grailsApplication.config.getProperty("app.pageImageRoot")

            for (task in Task.findAll()) {


                taskRows.add(new TaskRow(task: task, isNew: false))
            }
            for(book in Book.findAll()){
                book.discard()
                books.add(book)
            }
            for(userRole in UserRole.findAllByRole( Role.findByAuthority("ROLE_TYPIST"))){
                //println(userRole.user.toString())
                userRole.user.discard()
                typists.add(userRole.user)
            }
            for(userRole in UserRole.findAllByRole( Role.findByAuthority("ROLE_PROOFREADER"))){
                //println(userRole.user.toString())
                userRole.user.discard()
                proofreaders1.add(userRole.user)
                proofreaders2.add(userRole.user)
                proofreaders3.add(userRole.user)
            }


    }

    @Command
    def edit(@BindingParam('row') TaskRow row) {
        row.inEditing = !row.inEditing
        taskRows.set(taskRows.indexOf(row), row)
        pages.clear()
        def dir=new File("${pageImageRoot}/${row.task.book.classCode}")
        def pageImages=dir.listFiles()
        for(pageImage in pageImages){
            def name=FilenameUtils.getBaseName(pageImage.name)
            pages.add(name)
        }


    }
    @Command
    def publish(@BindingParam('row') TaskRow row){
        if(solrService.publish(Task.get(row.task.id))==0)
        {
            Clients.showNotification(Labels.getLabel("publishOK"))
        }
        else{
            Clients.showNotification(Labels.getLabel("publishNG"))
        }
    }
    @Command
    def save(@BindingParam('row') TaskRow row){
        if(row.isNew) {
            def task=new Task()
            task.typist=row.task.typist
            task.proofreader1=row.task.proofreader1
            task.proofreader2=row.task.proofreader2
            task.proofreader3=row.task.proofreader3
            task.book=row.task.book
            task.pageImageName=row.task.pageImageName
            task.status=row.task.status
            task.content=row.task.content
            row.task=task
        }else {

            def task=Task.get(row.task.id)
            task.typist=row.task.typist
            task.proofreader1=row.task.proofreader1
            task.proofreader2=row.task.proofreader2
            task.proofreader3=row.task.proofreader3
            task.book=row.task.book
            task.pageImageName=row.task.pageImageName
            task.status=row.task.status
            row.task=task
        }
        if(row.task.save(flush:true)){
            row.isNew=false
            row.inEditing = ! row.inEditing
            taskRows.set(taskRows.indexOf(row),row)
        }
        else{

            Messagebox.show(String.join("\n",row.task.errors.getAllErrors().collect {MessageFormat.format(it.defaultMessage,it.arguments)   }))
        }

    }
    @Command
    def cancel(@BindingParam('row') TaskRow row){
        row.inEditing = ! row.inEditing
        row.task=Task.get(row.task.id)
        taskRows.set(taskRows.indexOf(row),row)
        if(row.isNew){
            taskRows.remove(row)
        }
    }
    @Command
    @NotifyChange('tasks')
    def batchNewRow(){
        def fromIndex=fromPages.indexOf(batchFromPage)
        def toIndex=toPages.indexOf(batchToPage)
        for (def i=fromIndex;i<=toIndex;i++){
            def page=fromPages.get(i)
            def task=new Task(book: batchBook,pageImageName: page,status: "ToType",typist: batchTypist,proofreader1: batchProofreader1,proofreader2: batchProofreader2,proofreader3: batchProofreader3)
            task.save(flush: true)
            taskRows.add(new TaskRow(isNew: false,task: task))
        }
    }

    @NotifyChange(["fromPages","toPages","batchClassCode"])
    @Command
    def batchBookChange(){
        batchClassCode=batchBook.classCode
        fromPages.clear()
        toPages.clear()
        def dir=new File("${pageImageRoot}/${batchBook.classCode}")
        def pageImages=dir.listFiles().sort{it.name}
        for(pageImage in pageImages){
            def name=FilenameUtils.getBaseName(pageImage.name)
            fromPages.add(name)
            toPages.add(name)
        }
    }
    @Command
    def newRow(){
        taskRows.add(new TaskRow(task: new Task(status: "ToType",content: """<br class=\"lb_br\" style=\"display: none;\"/>&nbsp;"""),inEditing: true,isNew: true))

    }
    @Command
    @NotifyChange('pages')
    def bookChange(@BindingParam('row') TaskRow row)
    {
        pages.clear()
        def dir=new File("${pageImageRoot}/${row.task.book.classCode}")
        def pageImages=dir.listFiles()
        for(pageImage in pageImages){
            def name=FilenameUtils.getBaseName(pageImage.name)
            pages.add(name)
        }

    }
    @Command
    def delete(@BindingParam('row') TaskRow row){
        Messagebox.show( Labels.getLabel("confirm_message_delete"),Labels.getLabel("alert"),Messagebox.YES|Messagebox.NO, Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener(){
                    public void onEvent(Event evt) throws InterruptedException {
                        if (evt.getName().equals("onYes")) {
                            if (taskRows.contains(row))
                            {
                                taskRows.remove(row)
                            }
                            Task.get(row.task.id).delete(flush:true)
                        }
                        else{

                        }
                        //BindUtils.postNotifyChange(null, null, this, "*");
                    }})
    }
    @Command
    def downloadText(@BindingParam('row') TaskRow row) {
        Filedownload.save(Jsoup.parse(row.task.content).text(),"text/plain","HelloWorld.txt")

    }

}