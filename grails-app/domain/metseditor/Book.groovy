package metseditor

import groovy.transform.EqualsAndHashCode
import org.hibernate.annotations.GenericGenerator

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id

class Book {

    //作者
    String author
    //編譯者
    String editor
    //出版者
    String publisher
    //書名
    String title
    //叢書分冊、分卷
    String volume_title
    //書碼、經號
    String classCode
    //完書內容
    String content
    static hasMany = [tasks:Task]
    static constraints = {
        title nullable: false,blank: false
        classCode nullable: true,unique: true
        editor nullable: true
        publisher nullable: true
        content nullable: true
        author nullable: true
        volume_title nullable: true
    }
    static mapping={
        content type:"text"
    }

}
