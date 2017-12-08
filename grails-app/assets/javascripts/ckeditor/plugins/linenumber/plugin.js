CKEDITOR.plugins.add( 'linenumber', {
    requires: 'widget',

    icons: 'linenumber',
    toolbar:'mets',
    init: function( editor ) {

        var pluginDirectory = this.path;
        editor.addContentsCss( pluginDirectory + 'styles/linenumber.css' );
        editor.widgets.add( 'linenumber', {
            button: '插入行號標籤',
            toolbar:'mets',
            template:
            '<span class="lb" data-lineid="L01">&#8203;' +
            '</span>',
            requiredContent: 'span(lb)',
            upcast: function( element ) {
                return element.name == 'span' && element.hasClass( 'lb' );
            },
            init: function() {
                if(!this.element.hasAttribute("data-id")){
                    this.element.setAttribute("data-id",generateUUID());
                }

            },

            data: function() {
                var nodes=editor.document.getBody().find("span.lb");
                var pageNum = $("input[name='pageNum']").val();
                var i=0;
                this.element.setAttribute("data-lineid","P"+numeral(pageNum).format('000')+"L"+numeral(nodes.count()+1).format('00'));
                for(var i=0;i<nodes.count();i++){
                    if(nodes.getItem(i).getAttribute("data-id")==this.element.getAttribute("data-id")){
                        this.element.setAttribute("data-lineid","P"+numeral(pageNum).format('000')+"L"+numeral(i+1).format('00'));
                    }
                }

            }
        } );
        editor.setKeystroke( [

            [ CKEDITOR.CTRL + 76, 'linenumber' ]
        ] );
    }
} );
function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
};