package metseditor

import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class SolrServiceSpec extends HibernateSpec implements ServiceUnitTest<SolrService>{

    def setup() {
    }

    def cleanup() {
    }
    Map getConfiguration() {
        return ["dataSource.dbCreate": "create-drop","hibernate.cache.use_second_level_cache":"true","hibernate.cache.region.factory_class":"org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory"]
    }
    List<Class> getDomainClasses() { [User,Role,UserRole,Book,Task] }
    void "test something"() {
        when: 'book are already stored in db'
        def user = new User(username: "admin", name: "管理員", email: "chihchung.yu@gmail.com", password: "admin").save(flush: true)
        def user2 = new User(username: "typing", name: "打字義工", email: "chihchung.yu@gmail.com", password: "typing").save(flush: true)
        def user3 = new User(username: "proofread", name: "校對義工", email: "chihchung.yu@gmail.com", password: "proofread").save(flush: true)
        def role_admin = new Role(authority: "ROLE_ADMIN", name: "系統管理員").save(flush: true)
        def role_typist = new Role(authority: "ROLE_TYPIST", name: "打字義工").save(flush: true)
        def role_proofreader = new Role(authority: "ROLE_PROOFREADER", name: "校對義工").save(flush: true)
        new UserRole(user: user, role: role_admin).save(flush: true)
        new UserRole(user: user3, role: role_proofreader).save(flush: true)
        new UserRole(user: user2, role: role_typist).save(flush: true)
        def book = new Book(title: "善說金鬘論 上冊", author: "宗喀巴大師", editor: "", classCode: "A000002", publisher: "").save(flush: true)
        def book2 = new Book(title: "善說金鬘論 下冊", author: "宗喀巴大師", editor: "", classCode: "A000003", publisher: "").save(flush: true)
        def task = new Task(pageImageName: "IMG0011", typist: user2, proofreader1: user2, proofreader2: user2, proofreader3: user2, book: book, pageNum: "1", status: "Done", content: """<div>
<p><strong>གཞུང་དངོས།</strong><br/>།རྣམ་པ་ཐམས་ཅད་མཁྱེན་ཉིད་ལམ། །སྟོན་པས་འདི་ལས་བཤད་པ་གང་། །གཞན་གྱིས་མྱོང་བ་མ་ཡིན་<br/>
ཏེ། །ཆོས་སྤྱོད་བཅུ་ཡི་བདག་ཉིད་ཀྱི། །མདོ་དོན་དྲན་པ་ལ་བཞག་ནས། །བློ་དང་ལྡན་པས་མཐོང་འགྱུར་<br/>
ཕྱིར། །བདེ་བླག་ཏུ་ནི་རྟོགས་པ་ཞེས། །བྱ་བ་རྩོམ་པའི་དགོས་</p>
<p>པ་ཡིན། །ཤེས་རབ་ཕ་རོལ་ཕྱིན་པ་ནི། །དངོས་པོ་བརྒྱད་ཀྱིས་ཡང་དག་བཤད། །རྣམ་ཀུན་མཁྱེན་ཉིད་ལམ་<br/>
ཤེས་ཉིད། །དེ་ནས་ཐམས་ཅད་ཤེས་པ་ཉིད། །རྣམ་ཀུན་མངོན་རྫོགས་རྟོགས་པ་དང་། །རྩེ་མོར་ཕྱིན་དང་<br/>
མཐར་གྱིས་པ། །སྐད་ཅིག་གཅིག་མངོན་རྫོགས་བྱང་ཆུབ། །ཆོས་ཀྱི་སྐུ་དང་དེ་རྣམས་བརྒྱད། །སེམས་བསྐྱེད་<br/>
པ་དང་གདམས་ངག་དང་།</p>
</div>
""")
        task.save(flush: true)

        then: "fix me"
        service.publish(task) == 0
    }
}
