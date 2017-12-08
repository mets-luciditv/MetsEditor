package metseditor.user

import grails.util.Holders
import metseditor.User
import metseditor.UserService

class UserRow {
    User user
    boolean inEditing
    boolean isNew = false
    boolean isMemberOfAdmin
    boolean isMemberOfTypist
    boolean isMemberOfProofreader


}
