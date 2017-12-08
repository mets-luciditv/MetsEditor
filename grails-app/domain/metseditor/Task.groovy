package metseditor

import groovy.transform.EqualsAndHashCode

class Task {
    String pageImageName
    String content
    String status
    String pageNum
    static belongsTo = [book:Book,typist:User,proofreader1:User,proofreader2:User,proofreader3:User]
    static constraints = {
        content nullable: true,blank: true
        status inList: ["ToType","ToProofread1","ToProofread2","ToProofread3","Done"]
        pageNum nullable: true,blank: false
    }
    static mapping={
        content type:"text"
    }
}
