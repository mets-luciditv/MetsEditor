CKEDITOR.plugins.add( 'fix', {
    icons: 'fix',

    init: function( editor ) {
        editor.addCommand( 'fix', new CKEDITOR.dialogCommand( 'fixDialog' ,{
            readOnly:true,
            canUndo: true,
            contextSensitive:true,
            refresh: function( editor, path ) {
                if ( path.contains('del') || editor.getSelectedHtml(true).indexOf("del>")>=0)
                    this.setState( CKEDITOR.TRISTATE_DISABLED );
                else
                    this.setState( CKEDITOR.TRISTATE_OFF );
            }
        }) );
        editor.ui.addButton( 'fix', {
            label: '校正',
            command: 'fix',
            toolbar: 'basicstyles'
        });

        CKEDITOR.dialog.add( 'fixDialog', this.path + 'dialogs/fix.js' );
        editor.addContentsCss( this.path + 'styles/fix.css' );
    }
});