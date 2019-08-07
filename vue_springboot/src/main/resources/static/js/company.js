var app=new Vue({ //创建Vue实例
    el:"#app",     //获取div的对象
    data:{      //定义变量
        companyList:[],
        company:{}
    },
    methods:{       //定义方法
        findAll(){
            axios.get('company').then(
                function (response) {
                    alert(JSON.stringify(response)); //将json对象转字符串 JSON.parse
                    app.companyList=response.data;   //异步请求内无法使用this关键字
                }
            )
        },
        findOne(id){
            axios.get('company/'+id).then(  //方法传参
                function (response) {
                    //alert(JSON.stringify(response)) //将json对象转字符串 JSON.parse
                    app.company=response.data;
                }
            )
        },
        save(){
            if (this.company.id == null || this.company.id.length == 0){  //id没有值,那就是新增
                axios.post('company',this.company).then(
                    function (response) {
                        if (response.data.success){ //判断result对象的success属性
                            app.findAll();  //刷新
                        }else {
                            alert(response.data.message)
                        }
                    }
                )
            }else {
                axios.put('company', this.company).then(
                    function (response) {
                        if (response.data.success){ //判断result对象的success属性
                            app.findAll(); //刷新
                        }else {
                            alert(response.data.message)
                        }
                    }
                )
            }
        },
        deleteOne(id){
            axios.delete('company/'+id).then(
                function (response) {
                    if (response.data.success){ //判断result对象的success属性
                        app.findAll();  //刷新
                    }else {
                        alert(response.data.message)
                    }
                }
            )
        }
    },
    created(){
        this.findAll(); //进入页面就进行查询页面
        alert("加载了没有啊")
    },
})