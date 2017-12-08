CKEDITOR.dialog.add( 'fixDialog', function( editor ) {
    return {
        title: '校正',

        minWidth: 400,
        minHeight: 200,
        contents: [
            {
                id: 'tab-basic',
                label: 'Basic Settings',
                elements: [
                    {
                        type: 'text',
                        id: 'title',
                        label: '取代文字'
                    }
                ]
            }
        ],

        onOk: function() {
            var dialog = this;
            var seltext=editor.getSelectedHtml(true);
            var now=new Date();
            if(seltext !=null && seltext.length >0 ){
               var del= editor.document.createElement( 'del' );
               del.setHtml(seltext);
               del.setAttribute('datetime',now.toISOString());
               del.setAttribute("data-task-version",$("#task_version").val());
                del.setAttribute("data-username",$("#current_username").val());
                editor.setReadOnly(false);
                editor.insertElement(del);
                editor.setReadOnly(true);
            }

            var title=dialog.getValueOf( 'tab-basic', 'title' );
            if(title.length>0){
                var ins= editor.document.createElement( 'ins' );
                ins.setHtml(title.replace(new RegExp(" ", 'g'),"&nbsp;"));
                ins.setAttribute('datetime',now.toISOString());
                ins.setAttribute("data-task-version",$("#task_version").val());
                ins.setAttribute("data-username",$("#current_username").val());
                editor.setReadOnly(false);
                editor.insertElement(ins);
                editor.setReadOnly(true);
            }

        }
    };
});