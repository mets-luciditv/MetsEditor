CKEDITOR.dialog.add( 'noteDialog', function( editor ) {
    return {
        title: '註釋、校勘',

        minWidth: 400,
        minHeight: 200,
        contents: [
            {
                id: 'tab-basic',
                label: 'Basic Settings',
                elements: [
                    {
                        type: 'radio',
                        id: 'noteType',
                        label: '分類',
                        items: [ [ '原書註釋','gloss' ], [ '原書校勘','Proof' ] ],
                        'default': 'gloss'
                    },
                    {
                        type: 'text',
                        id: 'header',
                        label: '顯示文字'
                    },{
                        type: 'textarea',
                        id: 'notetext',
                        label: '內容'
                    }
                ]
            }
        ],
        onShow: function(sender){
            var editor = this.getParentEditor();
            var path=editor.elementPath();
            var span=path.contains( function( element ) {
                if ( element.is( 'span' ) && element.getAttribute( 'class' ) == 'note' )
                    return true;
            } );
            if(span) {
                var notetype=span.getAttribute('data-type');
                var head = span.findOne('span.notehead').getText();
                var text = span.findOne('textarea.notetext').getValue();
                var dialog = this;
                dialog.setValueOf('tab-basic', 'header', head);
                dialog.setValueOf('tab-basic', 'notetext', text);
                dialog.setValueOf('tab-basic', 'noteType', notetype);
            }


        },
        onOk: function() {
            var dialog = this;
            var editor = this.getParentEditor();
            var path=editor.elementPath();
            var span=path.contains( function( element ) {
                if ( element.is( 'span' ) && element.getAttribute( 'class' ) == 'note' )
                    return true;
            } );

            if(span) {
                span.setAttribute('data-type',dialog.getValueOf('tab-basic', 'noteType'));
                span.findOne('span.notehead').setHtml(dialog.getValueOf('tab-basic', 'header'));
                span.findOne('textarea.notetext').setHtml(dialog.getContentElement('tab-basic', 'notetext').getValue());
            }
            else{
                var note=editor.document.createElement( 'span' );
                note.setAttribute('class','note');
                note.setAttribute('data-type',dialog.getValueOf('tab-basic', 'noteType'));
                var head=editor.document.createElement( 'span' );
                head.setAttribute('class','notehead');
                head.setHtml(dialog.getValueOf('tab-basic', 'header'));
                var text=editor.document.createElement( 'textarea' );
                text.setAttribute('class','notetext');
                text.setHtml(dialog.getValueOf('tab-basic', 'notetext'));
                note.append(head);
                note.append(text);
                editor.setReadOnly(false);
                editor.insertElement(note);
                editor.setReadOnly(true);
            }
        }
    };
});