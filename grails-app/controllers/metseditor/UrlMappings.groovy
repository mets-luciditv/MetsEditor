package metseditor

class UrlMappings {

    static mappings = {
        "/PageImage/$classcode/$pagenum"(controller: "pageImage",action: "index")
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(redirect: '/index.zul')
        "500"(view:'/error')
        "404"(view:'/notFound')

    }
}
