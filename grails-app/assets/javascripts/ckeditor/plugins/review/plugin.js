CKEDITOR.plugins.add( 'review', {
    icons: 'review',
    init: function( editor ) {

        editor.addCommand( 'review', {
            readOnly:true,canUndo: true,
            contextSensitive:true,
            refresh: function( editor, path ) {
                if ( $("#cke_1_contents iframe").contents().find("del.review,ins.review,span.note.review,span.question.review").length>0)
                    this.setState( CKEDITOR.TRISTATE_ON );
                else
                    this.setState( CKEDITOR.TRISTATE_OFF );
            },
            exec: function( editor ) {
                if(this.state==CKEDITOR.TRISTATE_OFF){
                    $("#cke_1_contents iframe").contents().find("del,ins,span.note,span.question").addClass("review");
                    this.setState(CKEDITOR.TRISTATE_ON);
                }
                else{
                    $("#cke_1_contents iframe").contents().find("del,ins,span.note,span.question").removeClass("review");
                    this.setState(CKEDITOR.TRISTATE_OFF);
                }
            }
        });
        editor.ui.addButton( 'review', {
            label: 'Review mode',
            command: 'review',
            toolbar: ''
        });
    }
});