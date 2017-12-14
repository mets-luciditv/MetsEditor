CKEDITOR.dialog.add( 'note', function( editor ) {
    return {
        title: '註釋、校勘',

        minWidth: 400,
        minHeight: 200,
        contents: [
            {
                id: 'tab-basic',
                label: '註釋、校勘',
                elements: [
                    {
                        type: 'radio',
                        id: 'noteType',
                        label: '分類',
                        items: [ [ '註釋','gloss' ], [ '校勘','Proof' ] ],
                        setup:function(widget){
                            this.setValue(widget.data.noteType);
                        },
                        commit:function(widget){
                            widget.setData('noteType',this.getValue());
                        }
                    }
                    ,{
                        type: 'textarea',
                        id: 'noteContent',
                        label: '內容',
                        setup:function(widget){
                            this.setValue(widget.data.noteContent);
                        },
                        commit:function(widget){
                            widget.setData('noteContent',this.getValue());
                        }
                    }
                ]
            }
        ]
    };
});