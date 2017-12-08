package metseditor

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode(includes='classCode')
class Book {
    //作者
    String author
    //編譯者
    String editor
    //出版者
    String publisher
    //書名
    String title
    //書碼、經號
    String classCode
    static hasMany = [tasks:Task]
    static constraints = {
        title nullable: false,blank: false
        classCode nullable: false,unique: true,blank: false
        editor nullable: true
        publisher nullable: true
    }
}
