CKEDITOR.plugins.add( 'question', {
    icons: 'question',
    init: function( editor ) {
        var pluginDirectory = this.path;
        editor.addContentsCss( pluginDirectory + 'styles/question.css' );
        CKEDITOR.dialog.add( 'questionDialog', this.path + 'dialogs/question.js' );
        editor.addCommand( 'question', new CKEDITOR.dialogCommand( 'questionDialog' ,{
            readOnly:true,
            canUndo: true,
            contextSensitive:true
        }) );
        editor.ui.addButton( 'question', {
            label: '新增議題',
            command: 'question',
            toolbar: ''
        });

    }
});