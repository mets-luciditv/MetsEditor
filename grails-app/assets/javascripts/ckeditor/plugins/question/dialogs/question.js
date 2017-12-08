CKEDITOR.dialog.add( 'questionDialog', function( editor ) {
    return {
        title: '議題',

        minWidth: 400,
        minHeight: 200,
        contents: [
            {
                id: 'tab-basic',
                label: 'Basic Settings',
                elements: [
                    {
                        type: 'text',
                        id: 'header',
                        label: '顯示文字'
                    },{
                        type: 'textarea',
                        id: 'qtext',
                        label: '內容'
                    },{
                        type:'checkbox',
                        label:'已解決',
                        id:'solved'
                    }
                ]
            }
        ],
        onShow: function(sender){
            var editor = this.getParentEditor();
            var path=editor.elementPath();
            var span=path.contains( function( element ) {
                if ( element.is( 'span' ) && element.getAttribute( 'class' ) == 'question' )
                    return true;
            } );
            if(span) {
                var solved=span.getAttribute('solved');
                var head = span.findOne('span.qhead').getText();
                var text = span.findOne('textarea.qtext').getValue();
                var dialog = this;
                dialog.setValueOf('tab-basic', 'header', head);
                dialog.setValueOf('tab-basic', 'qtext', text);
                dialog.setValueOf('tab-basic', 'solved', solved=="true");
            }


        },
        onOk: function() {
            var dialog = this;
            var editor = this.getParentEditor();
            var path=editor.elementPath();
            var span=path.contains( function( element ) {
                if ( element.is( 'span' ) && element.getAttribute( 'class' ) == 'question' )
                    return true;
            } );

            if(span) {
                span.setAttribute('solved',dialog.getValueOf('tab-basic', 'solved'));
                span.findOne('span.qhead').setHtml(dialog.getValueOf('tab-basic', 'header'));
                span.findOne('textarea.qtext').setHtml(dialog.getContentElement('tab-basic', 'qtext').getValue());
            }
            else{
                var note=editor.document.createElement( 'span' );
                note.setAttribute('class','question');
                note.setAttribute('solved',dialog.getValueOf('tab-basic', 'solved'));
                var head=editor.document.createElement( 'span' );
                head.setAttribute('class','qhead');
                head.setHtml(dialog.getValueOf('tab-basic', 'header'));
                var text=editor.document.createElement( 'textarea' );
                text.setAttribute('class','qtext');
                text.setHtml(dialog.getValueOf('tab-basic', 'qtext'));
                note.append(head);
                note.append(text);
                editor.setReadOnly(false);
                editor.insertElement(note);
                editor.setReadOnly(true);
            }
        }
    };
});