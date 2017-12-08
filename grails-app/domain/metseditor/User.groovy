package metseditor


import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic


@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String name
    String password
    String email
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
   // static hasMany = [tasks:Task]
    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        email email:true,nullable:false, blank:false
        name nullable: false, blank:false
    }

    static mapping = {
	    password column: '`password`'
    }

    boolean isMemberOfRole(String authority){
        return authorities.any{it.authority.equals(authority)}
    }
    /*
    def beforeInsert() {
      //  createBy = springSecurityService.currentUser?.username
        this.password=this.encodePassword(this.password)
    }

    def beforeUpdate() {
       // lastUpdateBy = springSecurityService.currentUser?.username
       // lastUpdated = new Date()
        if(this.isDirty("password")){
           this.password=this.encodePassword(this.password)
        }
    }
    private String encodePassword(String password) {
       springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }
    */
}
