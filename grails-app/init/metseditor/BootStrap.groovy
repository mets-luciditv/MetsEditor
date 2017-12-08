package metseditor

import grails.plugin.springsecurity.SecurityFilterPosition
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.util.Environment
import org.springframework.security.core.context.SecurityContextHolder

class BootStrap {

    def init = { servletContext ->
        SpringSecurityUtils.registerFilter('concurrenctSessionFilter', SecurityFilterPosition.CONCURRENT_SESSION_FILTER)
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL)
        if(Environment.current== Environment.PRODUCTION){
            if(Role.findByAuthority("ROLE_ADMIN")==null){
                def role_admin = new Role(authority: "ROLE_ADMIN",name:"系統管理員").save(flush: true)
            }
            if(Role.findByAuthority("ROLE_TYPIST")==null){
                def role_typist = new Role(authority: "ROLE_TYPIST", name:"打字義工").save(flush: true)
            }
            if(Role.findByAuthority("ROLE_PROOFREADER")==null){
                def role_proofreader = new Role(authority: "ROLE_PROOFREADER",name:"校對義工").save(flush: true)
            }
            if(User.findByUsername("admin")==null){
                def user = new User(username: "admin",name:"管理員",email: "chihchung.yu@gmail.com", password: "admin").save(flush: true)
                new UserRole(user: user,role: Role.findByAuthority("ROLE_ADMIN")).save(flush: true)
            }
        }
        if(Environment.current==  Environment.TEST){
            if(Role.findByAuthority("ROLE_ADMIN")==null){
                def role_admin = new Role(authority: "ROLE_ADMIN",name:"系統管理員").save(flush: true)
            }
            if(Role.findByAuthority("ROLE_TYPIST")==null){
                def role_typist = new Role(authority: "ROLE_TYPIST", name:"打字義工").save(flush: true)
            }
            if(Role.findByAuthority("ROLE_PROOFREADER")==null){
                def role_proofreader = new Role(authority: "ROLE_PROOFREADER",name:"校對義工").save(flush: true)
            }
            if(User.findByUsername("admin")==null){
                def user = new User(username: "admin",name:"管理員",email: "chihchung.yu@gmail.com", password: "admin").save(flush: true)
                new UserRole(user: user,role: Role.findByAuthority("ROLE_ADMIN")).save(flush: true)
            }
        }
        if(Environment.current == Environment.DEVELOPMENT){
            def user = new User(username: "admin",name:"管理員",email: "chihchung.yu@gmail.com", password: "admin").save(flush: true)
            def user2 = new User(username: "typing",name:"打字義工",email: "chihchung.yu@gmail.com", password: "typing").save(flush: true)
            def user3 = new User(username: "proofread",name:"校對義工",email: "chihchung.yu@gmail.com", password: "proofread").save(flush: true)
            def role_admin = new Role(authority: "ROLE_ADMIN",name:"系統管理員").save(flush: true)
            def role_typist = new Role(authority: "ROLE_TYPIST", name:"打字義工").save(flush: true)
            def role_proofreader = new Role(authority: "ROLE_PROOFREADER",name:"校對義工").save(flush: true)
            new UserRole(user: user,role: role_admin).save(flush: true)
            new UserRole(user: user3,role: role_proofreader).save(flush: true)
            new UserRole(user: user2,role: role_typist).save(flush: true)
            def book=new Book(title:"善說金鬘論 上冊",author: "宗喀巴大師", editor: "",classCode: "A000002",publisher: "").save(flush: true)
            def book2=new Book(title:"善說金鬘論 下冊",author: "宗喀巴大師", editor: "",classCode: "A000003",publisher: "").save(flush: true)
            def task=new Task(pageImageName: "IMG0011",typist: user,proofreader3: user,proofreader2: user,proofreader1: user,book:book,content: """<div>
<p><strong>གཞུང་དངོས།</strong><br/>
&nbsp; &nbsp; །རྣམ་པ་ཐམས་ཅད་མཁྱེན་ཉིད་ལམ། །སྟོན་པས་འདི་ལས་བཤད་པ་གང་། །གཞན་གྱིས་མྱོང་བ་མ་ཡིན་<br/>
ཏེ། །ཆོས་སྤྱོད་བཅུ་ཡི་བདག་ཉིད་ཀྱི། །མདོ་དོན་དྲན་པ་ལ་བཞག་ནས། །བློ་དང་ལྡན་པས་མཐོང་འགྱུར་<br/>
ཕྱིར། །བདེ་བླག་ཏུ་ནི་རྟོགས་པ་ཞེས། །བྱ་བ་རྩོམ་པའི་དགོས་<br/>
པ་ཡིན། །ཤེས་རབ་ཕ་རོལ་ཕྱིན་པ་ནི། །དངོས་པོ་བརྒྱད་ཀྱིས་ཡང་དག་བཤད། །རྣམ་ཀུན་མཁྱེན་ཉིད་ལམ་<br/>
ཤེས་ཉིད། །དེ་ནས་ཐམས་ཅད་ཤེས་པ་ཉིད། །རྣམ་ཀུན་མངོན་རྫོགས་རྟོགས་པ་དང་། །རྩེ་མོར་ཕྱིན་དང་<br/>
མཐར་གྱིས་པ། །སྐད་ཅིག་གཅིག་མངོན་རྫོགས་བྱང་ཆུབ། །ཆོས་ཀྱི་སྐུ་དང་དེ་རྣམས་བརྒྱད། །སེམས་བསྐྱེད་<br/>
པ་དང་གདམས་ངག་དང་།</p>
</div>
""",status: "ToType").save(flush: true)

        }
    }
    def destroy = {
    }
}
