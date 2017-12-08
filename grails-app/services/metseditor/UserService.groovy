package metseditor

import grails.gorm.transactions.Transactional
import grails.validation.ValidationException

@Transactional
class UserService {

    def getRoles(User user){
        def isMemberOfAdmin = user.authorities.any {it.authority.equals("ROLE_ADMIN")}
        def isMemberOfTypist = user.authorities.any {it.authority.equals("ROLE_TYPIST")}
        def isMemberOfProofreader = user.authorities.any {it.authority.equals("ROLE_PROOFREADER")}
        return [isMemberOfAdmin ,isMemberOfTypist, isMemberOfProofreader]
    }
    def save(User user,boolean isMemberOfAdmin,boolean isMemberOfTypist,boolean isMemberOfProofreader){
        if(!user.save(flush:true)){
            throw new ValidationException("User is not valid", user.errors)
        }
        def result=  setUserRole( user,"ROLE_ADMIN",isMemberOfAdmin) && setUserRole( user,"ROLE_TYPIST",isMemberOfTypist) && setUserRole( user,"ROLE_PROOFREADER",isMemberOfProofreader)
        if (!result){
            throw new Exception("Set Role fail.")
        }
        return true
    }

    def setUserRole(User user,String authority,boolean isMemberOfRole){
        Role role = Role.findByAuthority(authority)
        if(isMemberOfRole){
            if(!UserRole.exists(user.id,role.id)){
                return new UserRole(user: user,role: role).save(flush:true)
            }
        }else{
            if(UserRole.exists(user.id,role.id)){
                return UserRole.remove(user,role)
            }
        }
        return  true

    }
}
