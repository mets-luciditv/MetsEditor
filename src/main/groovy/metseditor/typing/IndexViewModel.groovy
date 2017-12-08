package metseditor.typing

import grails.plugin.springsecurity.SpringSecurityUtils
import metseditor.Book
import metseditor.Role
import metseditor.Task
import metseditor.User
import metseditor.UserRole
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.filefilter.WildcardFileFilter
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
import org.zkoss.zul.ListModelList
import org.zkoss.zul.Messagebox

import javax.imageio.ImageIO
import java.text.MessageFormat

@VariableResolver(DelegatingVariableResolver.class)
class IndexViewModel {
    @WireVariable springSecurityService
    ListModelList<TaskRow> taskRows = new ListModelList<TaskRow>()
    ListModelList<Book> books = new ListModelList<Book>()
    ListModelList<User> typists = new ListModelList<User>()
    ListModelList<User> proofreaders1 = new ListModelList<User>()
    ListModelList<User> proofreaders2 = new ListModelList<User>()
    ListModelList<User> proofreaders3 = new ListModelList<User>()
    List<String> pages=new ArrayList<String>()
    @WireVariable def grailsApplication

    String pageImageRoot
    @Init
    def init() {
        pageImageRoot=grailsApplication.config.getProperty("app.pageImageRoot")

        for (task in Task.findAllByStatusAndTypist("ToType",springSecurityService.getCurrentUser() as User)) {

            taskRows.add(new TaskRow(task: task, isNew: false))
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
    def save(@BindingParam('row') TaskRow row){
        if(row.isNew) {
            def task=new Task()
            task.typist=row.task.typist
            task.proofreader1=row.task.proofreader1
            task.proofreader2=row.task.proofreader2
            task.proofreader3=row.task.proofreader3
            task.book=row.task.book
            task.pageImageName=row.task.pageImageName
            row.task=task
        }else {

            def task=Task.get(row.task.id)
            task.typist=row.task.typist
            task.proofreader1=row.task.proofreader1
            task.proofreader2=row.task.proofreader2
            task.proofreader3=row.task.proofreader3
            task.book=row.task.book
            task.pageImageName=row.task.pageImageName
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
    }
    @Command
    def newRow(){
        taskRows.add(new TaskRow(task: new Task(),inEditing: true,isNew: true))

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
    def open(@BindingParam('row') TaskRow row){
        def classCode=row.task.book.classCode
        def pageNum=row.task.pageImageName

        def dir=new File("${pageImageRoot}/${classCode}")
        def fileFilter = new WildcardFileFilter("${pageNum}.*")
        def pageImage=dir.listFiles((FileFilter)fileFilter).first()
        def img=ImageIO.read(pageImage)
        def width=img.width
        def height=img.height
        if(height>width){
            Executions.getCurrent().sendRedirect("./openH.zul?task=${row.task.id}","_blank")
        }
        else{
            Executions.getCurrent().sendRedirect("./openV.zul?task=${row.task.id}","_blank")
        }
    }
}