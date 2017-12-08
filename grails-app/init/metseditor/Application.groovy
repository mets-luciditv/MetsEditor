package metseditor

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration
import org.springframework.context.annotation.ComponentScan

@ComponentScan
@EnableAutoConfiguration(exclude = [SolrAutoConfiguration])
class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }
}