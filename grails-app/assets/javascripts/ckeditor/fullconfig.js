CKEDITOR.plugins.addExternal( 'lineutils', '/MetsEditor/assets/ckeditor/plugins/lineutils/','plugin.js' );
CKEDITOR.plugins.addExternal( 'widgetselection', '/MetsEditor/assets/ckeditor/plugins/widgetselection/','plugin.js' );
CKEDITOR.plugins.addExternal( 'widget', '/MetsEditor/assets/ckeditor/plugins/widget/','plugin.js' );
CKEDITOR.plugins.addExternal( 'linenumber', '/MetsEditor/assets/ckeditor/plugins/linenumber/','plugin.js' );
CKEDITOR.plugins.addExternal( 'mets_enterkey', '/MetsEditor/assets/ckeditor/plugins/mets_enterkey/','plugin.js' );
CKEDITOR.plugins.addExternal( 'showlinenumber', '/MetsEditor/assets/ckeditor/plugins/showlinenumber/','plugin.js' );
CKEDITOR.plugins.addExternal( 'lineheight', '/MetsEditor/assets/ckeditor/plugins/lineheight/','plugin.js' );
CKEDITOR.plugins.addExternal( 'note', '/MetsEditor/assets/ckeditor/plugins/note/','plugin.js' );
CKEDITOR.editorConfig = function(config) {
    config.contentsCss = ['/MetsEditor/assets/ckeditor/contents.css','/MetsEditor/assets/css/fontawesome-all.css'];
    config.font_names='Monlam Uni OuChan2;'+config.font_names;
   config.extraPlugins = 'linenumber,mets_enterkey,showlinenumber,lineheight,note';
    config.allowedContent=true;
    config.toolbar = [
        { name: 'document', items: [ 'Source', '-', 'Save', 'NewPage', 'Preview', 'Print', '-', 'Templates' ] },
        { name: 'clipboard', items: [ 'Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord', '-', 'Undo', 'Redo' ] },
        { name: 'editing', items: [ 'Find', 'Replace', '-', 'SelectAll', '-', 'Scayt' ] },
        '/',
        { name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'CopyFormatting', 'RemoveFormat' ] },
        { name: 'paragraph', items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', 'CreateDiv', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', '-', 'BidiLtr', 'BidiRtl', 'Language' ] },
        { name: 'insert', items: [ 'Image',  'Table', 'HorizontalRule', 'Smiley', 'SpecialChar', 'PageBreak' ] },
        '/',
        { name: 'styles', items: [ 'Styles', 'Format', 'Font', 'FontSize','lineheight' ] },
        { name: 'colors', items: [ 'TextColor', 'BGColor' ] },
        { name: 'tools', items: [ 'Maximize', 'ShowBlocks' ] },
        { name: 'mets' , items: ['Linenumber','showlinenumber','Note']}
    ];
    //config.newpage_html = '<br class="lb_br" style="display: none;"/>&nbsp;';
    //config.protectedSource.push(/<span[^>]*><\/span>/g);
};
