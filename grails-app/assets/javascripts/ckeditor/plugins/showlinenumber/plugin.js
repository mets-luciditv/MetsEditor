CKEDITOR.plugins.add( 'showlinenumber', {
    icons: 'showlinenumber',
    init: function( editor ) {
        editor.addCommand( 'showlinenumber', {
            state:CKEDITOR.TRISTATE_ON,
            contextSensitive:true,
            refresh: function( editor, path ) {
                if ( editor.document.getBody().hasClass("lb_hidden"))
                    this.setState( CKEDITOR.TRISTATE_OFF );
                else
                    this.setState( CKEDITOR.TRISTATE_ON );
            },
            exec: function( editor ) {
                if(this.state==CKEDITOR.TRISTATE_OFF){
                    editor.document.getBody().removeClass('lb_hidden');
                    this.setState(CKEDITOR.TRISTATE_ON);
                }
                else{
                    editor.document.getBody().addClass('lb_hidden');
                    this.setState(CKEDITOR.TRISTATE_OFF);
                }
            }
        });
        editor.ui.addButton( 'showlinenumber', {
            label: '顯示行號',
            command: 'showlinenumber',
            toolbar: 'mets'
        });
    }
});