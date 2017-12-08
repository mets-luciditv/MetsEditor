package metseditor.proofread

import grails.plugin.springsecurity.SpringSecurityService
import metseditor.Task
import metseditor.User
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
import org.zkoss.zul.Messagebox

@VariableResolver(DelegatingVariableResolver.class)
class OpenViewModel {
    Task task
    String status
    @WireVariable SpringSecurityService springSecurityService
    @Init
    def init(@BindingParam('status') String status){
        this.status=status
        def taskId=Long.parseLong(Executions.getCurrent().getParameter("task"))

        switch (status){
            case "ToProofread1":
                task= Task.findByIdAndProofreader1AndStatus(taskId,springSecurityService.getCurrentUser() as User,status)
                Executions.current.desktop.getFirstPage().setTitle("${Labels.getLabel('proofreader1')}(${task.book.classCode}-${task.pageImageName})")
                break
            case "ToProofread2":
                task= Task.findByIdAndProofreader2AndStatus(taskId,springSecurityService.getCurrentUser() as User,status)
                Executions.current.desktop.getFirstPage().setTitle("${Labels.getLabel('proofreader2')}(${task.book.classCode}-${task.pageImageName})")
                break
            case "ToProofread3":
                task= Task.findByIdAndProofreader3AndStatus(taskId,springSecurityService.getCurrentUser() as User,status)
                Executions.current.desktop.getFirstPage().setTitle("${Labels.getLabel('proofreader3')}(${task.book.classCode}-${task.pageImageName})")
                break
        }
        if(task==null) {
            switch (status){
                case "ToProofread1":
                    Executions.current.sendRedirect('/proofread1/index.zul')
                    break
                case "ToProofread2":
                    Executions.current.sendRedirect('/proofread2/index.zul')
                    break
                case "ToProofread3":
                    Executions.current.sendRedirect('/proofread3/index.zul')
                    break
            }
        }
        else{
            task.book.discard()
            task.typist.discard()
            task.proofreader1.discard()
            task.proofreader2.discard()
            task.proofreader3.discard()
         //   Executions.current.desktop.getFirstPage().setTitle("${Labels.getLabel('proofread')}(${task.book.classCode}-${task.pageImageName})")
        }

    }
    @Command
    def proofreadDone(){
            Messagebox.show( Labels.getLabel("confirm_message_proofreadDone"),Labels.getLabel("alert"),Messagebox.YES|Messagebox.NO,Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener(){
                        public void onEvent(Event evt) throws InterruptedException {
                            if (evt.getName().equals("onYes")) {
                                def _task=Task.get(task.id)

                                switch (status){
                                    case "ToProofread1":
                                        _task.status="ToProofread2"
                                        _task.save(flush:true)
                                        Executions.current.sendRedirect('/proofread1/index.zul')
                                        break
                                    case "ToProofread2":
                                        _task.status="ToProofread3"
                                        _task.save(flush:true)
                                        Executions.current.sendRedirect('/proofread2/index.zul')
                                        break
                                    case "ToProofread3":
                                        _task.status="Done"
                                        _task.save(flush:true)
                                        Executions.current.sendRedirect('/proofread3/index.zul')
                                        break
                                }
                            }
                            else{
                            }
                        }})


    }
    @Command
    @NotifyChange('task')
    def save(){
        def _task=Task.get(task.id)
        _task.content=task.content.replace("&#8203;","")
        _task.pageNum=task.pageNum
        _task.save(flush:true)
        task.version=_task.version
        if(!_task.hasErrors()){
            Clients.showNotification(Labels.getLabel("saveOK"))
        }
        else {
            Clients.showNotification(Labels.getLabel("saveNG"))
        }
    }
}
