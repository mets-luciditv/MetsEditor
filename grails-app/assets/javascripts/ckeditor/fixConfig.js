CKEDITOR.plugins.addExternal( 'fix', '/MetsEditor/assets/ckeditor/plugins/fix/','plugin.js' );
CKEDITOR.plugins.addExternal( 'review', '/MetsEditor/assets/ckeditor/plugins/review/','plugin.js' );
CKEDITOR.plugins.addExternal( 'note', '/MetsEditor/assets/ckeditor/plugins/note/','plugin.js' );
CKEDITOR.plugins.addExternal( 'question', '/MetsEditor/assets/ckeditor/plugins/question/','plugin.js' );
CKEDITOR.editorConfig = function(config) {
    config.font_names='Monlam Uni OuChan2';
    // config.toolbar = [['Save', 'Bold','PageBreak','Font','FontSize' ]];
    config.toolbar = [['Source','-','fix','review','note','question']];
    config.extraPlugins = 'fix,review,note,question';
    config.readOnly = true;
    config.enterMode = CKEDITOR.ENTER_BR;
    config.shiftEnterMode = CKEDITOR.ENTER_P;
    config.allowedContent=true;
    config.keystrokes=[
        [CKEDITOR.CTRL+78,'note'],
        [CKEDITOR.CTRL+81,'question'],
        [CKEDITOR.CTRL+70,'fix'],
        [CKEDITOR.CTRL+80,'review']
    ];
};
