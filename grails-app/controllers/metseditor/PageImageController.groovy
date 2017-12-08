package metseditor

import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.filefilter.WildcardFileFilter
import org.grails.core.io.ResourceLocator
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource

class PageImageController {

    @Value('${app.pageImageRoot}')
    String pageImageRoot
    def index(){

        def classCode=params.classcode
        def pageNum=params.pagenum

        def dir=new File("${pageImageRoot}/${classCode}")
        def fileFilter = new WildcardFileFilter("${pageNum}.*")
        def pageImage=dir.listFiles((FileFilter)fileFilter).first()
        def extension=FilenameUtils.getExtension(pageImage.name)

        render file: pageImage , contentType: grailsApplication.config.getProperty("grails.mime.types.${extension}")
        /*
        BufferedIxmage originalImage = ImageIO.read(pageImage)
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
        ImageIO.write(originalImage, extension, outputStream)
        byte[] imageInByte = outputStream.toByteArray()
        response.setHeader("Content-Length", imageInByte.length.toString())
        response.contentType = grailsApplication.config.getProperty("grails.mime.types.${extension}")
        response.outputStream << imageInByte
        response.outputStream.flush()
         */
    }
}
