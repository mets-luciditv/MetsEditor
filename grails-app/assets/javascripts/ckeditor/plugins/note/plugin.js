CKEDITOR.plugins.add( 'note', {
    icons: 'note',
    init: function( editor ) {
        var pluginDirectory = this.path;
        editor.addContentsCss( pluginDirectory + 'styles/note.css' );
        CKEDITOR.dialog.add( 'noteDialog', this.path + 'dialogs/note.js' );
        editor.addCommand( 'note', new CKEDITOR.dialogCommand( 'noteDialog' ,{
            readOnly:true,
            canUndo: true,
            contextSensitive:true
        }) );
        editor.ui.addButton( 'note', {
            label: '新增註釋',
            command: 'note',
            toolbar: ''
        });

    }
});