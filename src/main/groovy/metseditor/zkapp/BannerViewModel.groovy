package metseditor.zkapp

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.GlobalCommand
import org.zkoss.bind.annotation.Init
import org.zkoss.spring.DelegatingVariableResolver
import org.zkoss.util.resource.Labels
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.VariableResolver
import org.zkoss.zk.ui.select.annotation.WireVariable

@VariableResolver(DelegatingVariableResolver.class)
class BannerViewModel {
    @WireVariable
    SpringSecurityService springSecurityService
    @WireVariable SpringSecurityUtils springSecurityUtils
    @Init
    def init(){

    }
    @GlobalCommand
    def onNavigate(@BindingParam('name') String name,@BindingParam('uri') String locationUri){
        //redirect current url to new location
        if(locationUri.startsWith("http")){
            //open a new browser tab
            Executions.getCurrent().sendRedirect(locationUri)
        } else {
            Executions.getCurrent().sendRedirect(locationUri)

        }
    }
}
