package metseditor.proofread

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
import metseditor.proofread.TaskRow

import javax.imageio.ImageIO
import java.text.MessageFormat

@VariableResolver(DelegatingVariableResolver.class)
class IndexViewModel {
    @WireVariable springSecurityService
    ListModelList<TaskRow> taskRows = new ListModelList<TaskRow>()
    ListModelList<Book> books = new ListModelList<Book>()
    ListModelList<User> users= new ListModelList<User>()
    List<String> pages=new ArrayList<String>()
    @WireVariable def grailsApplication

    String pageImageRoot
    @Init
    def init(@BindingParam('status') String status) {
        pageImageRoot=grailsApplication.config.getProperty("app.pageImageRoot")
        switch (status){
            case "ToProofread1":
                for (task in Task.findAllByProofreader1AndStatus(springSecurityService.getCurrentUser() as User,status)) {

                    taskRows.add(new TaskRow(task: task, isNew: false))
                }
                break
            case "ToProofread2":
                for (task in Task.findAllByProofreader2AndStatus(springSecurityService.getCurrentUser() as User,status)) {

                    taskRows.add(new TaskRow(task: task, isNew: false))
                }
                break
            case "ToProofread3":
                for (task in Task.findAllByProofreader3AndStatus(springSecurityService.getCurrentUser() as User,status)) {

                    taskRows.add(new TaskRow(task: task, isNew: false))
                }
                break
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

        }else {

            def task=Task.get(row.task.id)
            task.typist=row.task.typist
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
    def delete(@BindingParam('row') metseditor.proofread.TaskRow row){
        Messagebox.show( Labels.getLabel("confirm_message_delete"),Labels.getLabel("alert"),Messagebox.YES|Messagebox.NO, Messagebox.QUESTIONM,
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