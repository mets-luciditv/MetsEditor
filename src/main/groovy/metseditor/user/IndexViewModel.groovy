package metseditor.user


import groovy.util.logging.Commons
import metseditor.User
import org.zkoss.bind.annotation.BindingParam
import org.zkoss.bind.annotation.Command
import org.zkoss.bind.annotation.Init
import org.zkoss.spring.DelegatingVariableResolver
import org.zkoss.zk.ui.Executions
import org.zkoss.zk.ui.select.annotation.VariableResolver
import org.zkoss.zk.ui.select.annotation.WireVariable
import org.zkoss.zul.ListModelList

/**
 * Created by yup on 2017/6/27.
 */
@Commons
@VariableResolver(DelegatingVariableResolver.class)
class IndexViewModel {
    ListModelList<UserRow> userRows= new ListModelList<UserRow>()

    User selectedUser

    @WireVariable userService
    @WireVariable springSecurityService
    @Init
    def init(){
        for(user in User.findAll()){
            def (isAdmim,isTypist,isProof) = userService.getRoles(user)
            userRows.add(new UserRow(user: user,inEditing:  false,isMemberOfAdmin: isAdmim,isMemberOfTypist: isTypist,isMemberOfProofreader: isProof))
        }
    }

    @Command
    def edit(@BindingParam('row') UserRow row){
        row.inEditing = ! row.inEditing
        row.user=User.get(row.user.id)
        userRows.set(userRows.indexOf(row),row)
    }

    @Command
    def save(@BindingParam('row') UserRow row){
        if(row.isNew) {


        }else {

            def user=User.get(row.user.id)
            user.name=row.user.name
            user.username=row.user.username
            user.email=row.user.email
            user.password=row.user.password
            user.enabled=row.user.enabled
            row.user=user
        }
        if(userService.save(row.user,  row.isMemberOfAdmin,  row.isMemberOfTypist,  row.isMemberOfProofreader)){
            row.isNew=false
            row.inEditing = ! row.inEditing
            userRows.set(userRows.indexOf(row),row)
        }
        if (springSecurityService.loggedIn &&
                springSecurityService.principal.username == row.user.username) {
            springSecurityService.reauthenticate row.user.username
            Executions.current.sendRedirect("")
        }
    }
    @Command
    def cancel(@BindingParam('row') UserRow row){
        row.inEditing = ! row.inEditing
        userRows.set(userRows.indexOf(row),row)
    }
    @Command
    def newRow(){
        userRows.add(new UserRow(user:new User(),inEditing: true,isMemberOfAdmin: false,isMemberOfTypist: false,isMemberOfProofreader: false,isNew: true))

    }

}