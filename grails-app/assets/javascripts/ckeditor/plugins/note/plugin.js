CKEDITOR.plugins.add( 'note', {
    requires: 'widget',
    icons: 'note',
    init: function( editor ) {

        var pluginDirectory = this.path;
        editor.addContentsCss( pluginDirectory + 'styles/note.css' );
        CKEDITOR.dialog.add( 'note', this.path + 'dialogs/note.js' );
        editor.widgets.add( 'note', {
            button: '插入註釋、校勘',
            template:
            '<span class="note" ><span class="note-title">&#8203;</span>' +
            '</span>',
            requiredContent: 'span(note)',
            upcast: function( element ) {
                return element.name == 'span' && element.hasClass( 'note' );
            },
            dialog: 'note',
            init: function() {
                if(!this.element.hasAttribute("data-id")){
                    this.element.setAttribute("data-id",generateUUID());
                }

                var noteType = this.element.getAttribute( 'data-note-type' );
                if ( noteType )
                    this.setData( 'noteType', noteType );
                else
                    this.setData( 'noteType', 'gloss' );

                var noteContent = this.element.getAttribute( 'data-note-content' );
                if ( noteContent )
                    this.setData( 'noteContent', CKEDITOR.tools.htmlDecode(noteContent ));

                if(this.editor.getSelectedHtml()) {
                    var selectedHtml = this.editor.getSelectedHtml().getHtml();
                    if (selectedHtml) {
                        this.element.findOne(".note-title").appendHtml(selectedHtml);
                    }
                }

            },

            data: function() {

                this.element.setAttribute("data-note-type",this.data.noteType);
                this.element.setAttribute("data-note-content",CKEDITOR.tools.htmlEncode(this.data.noteContent));
                var nodes=editor.document.getBody().find("span.note[data-note-type="+this.data.noteType+"]");
                this.element.setAttribute("data-note-no",nodes.count()+1);
                for(var i=0;i<nodes.count();i++){
                    if(nodes.getItem(i).getAttribute("data-id")==this.element.getAttribute("data-id")){
                        this.element.setAttribute("data-note-no",i+1);
                    }
                }

            }
        } );
        editor.setKeystroke( [

           [ CKEDITOR.CTRL + 78, 'note' ]
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