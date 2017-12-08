CKEDITOR.plugins.addExternal( 'note', '/MetsEditor/assets/ckeditor/plugins/note/','plugin.js' );
CKEDITOR.plugins.addExternal( 'question', '/MetsEditor/assets/ckeditor/plugins/question/','plugin.js' );
CKEDITOR.editorConfig = function(config) {
    config.font_names='Monlam Uni OuChan2';
    config.extraPlugins = 'note,question';
    config.toolbar = [['Source','Undo','Redo','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-', 'Bold','PageBreak','HorizontalRule','Font','FontSize','TextColor','Superscript','note','question']];
   // config.enterMode = CKEDITOR.ENTER_BR;
  //  config.shiftEnterMode = CKEDITOR.ENTER_P;
    config.allowedContent=true;
    config.protectedSource.push(/<span[^>]*><\/span>/g);
    config.keystrokes=[
        [CKEDITOR.CTRL+78,'note'],
        [CKEDITOR.CTRL+81,'question']
    ];
};
