package metseditor.typing

import grails.plugin.springsecurity.SpringSecurityService
import metseditor.Task
import metseditor.User
import org.zkoss.bind.annotation.Init
import org.zkoss.spring.DelegatingVariableResolver
import org.zkoss.util.resource.Labels
import org.zkoss.zk.ui.Desktop
import org.zkoss.zk.ui.Executions
import org.zkoss.bind.annotation.Command
import org.zkoss.zk.ui.Page
import org.zkoss.zk.ui.event.Event
import org.zkoss.zk.ui.select.annotation.VariableResolver
import org.zkoss.zk.ui.select.annotation.WireVariable
import org.zkoss.zk.ui.util.Clients
import org.zkoss.zul.Messagebox

import java.text.MessageFormat

@VariableResolver(DelegatingVariableResolver.class)
class OpenViewModel {
    Task task
    @WireVariable SpringSecurityService springSecurityService
    @Init
    def init(){
        def taskId=Long.parseLong(Executions.getCurrent().getParameter("task"))
        task= Task.findByIdAndTypistAndStatus(taskId,springSecurityService.getCurrentUser() as User,"ToType")
        if(task==null) {
            Executions.current.sendRedirect('/typing/index.zul')
        }
        else{
            task.book.discard()
            task.typist.discard()
            Executions.current.desktop.getFirstPage().setTitle("${Labels.getLabel('typing')}(${task.book.classCode}-${task.pageImageName})")
        }

    }
    @Command
    def typingDone(){


            Messagebox.show( Labels.getLabel("confirm_message_typingDone"),Labels.getLabel("alert"),Messagebox.YES|Messagebox.NO,Messagebox.QUESTION,
                    new org.zkoss.zk.ui.event.EventListener(){
                        public void onEvent(Event evt) throws InterruptedException {

                            if (evt.getName().equals("onYes")) {
                                def _task=Task.get(task.id)
                                _task.status="ToProofread1"
                                _task.save(flush:true)
                                Executions.current.sendRedirect('/typing/index.zul')
                            }
                            else{

                            }
                        }})


    }
    @Command
    def save(){
        def _task=Task.get(task.id)
        _task.content=task.content
        _task.pageNum=task.pageNum
        _task.save(flush:true)
        if(!_task.hasErrors()){
            Clients.showNotification(Labels.getLabel("saveOK"))
        }
        else {
            Clients.showNotification(String.join("\n",_task.errors.getAllErrors().collect {MessageFormat.format(it.defaultMessage,it.arguments)   }))
        }
    }
}
