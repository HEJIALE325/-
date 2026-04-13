<template>
    <div class="main-content">

        <!-- 条件查询 -->
        <div v-if="showFlag">
            <el-form :inline="true" :model="searchForm" class="form-content">
                <el-row :gutter="20" class="slt" :style="{justifyContent:contents.searchBoxPosition=='1'?'flex-start':contents.searchBoxPosition=='2'?'center':'flex-end'}">
                 
                     <el-form-item :label="contents.inputTitle == 1 ? '宠物名称' : ''">
                         <el-input prefix-icon="el-icon-search" v-model="searchForm.name" placeholder="宠物名称" clearable></el-input>
                     </el-form-item>
                          
                     <el-form-item :label="contents.inputTitle == 1 ? '分类' : ''">
                        <el-select v-model="searchForm.categoryId" placeholder="请选择分类">
                            <el-option label="=-请选择-=" value=""></el-option>
                            <el-option
                               v-for="(item,index) in categorySelectSearch"
                               v-bind:key="index"
                               :label="item.name"
                               :value="item.id">
                            </el-option>
                        </el-select>
                     </el-form-item>

                     <el-form-item :label="contents.inputTitle == 1 ? '状态' : ''">
                        <el-select v-model="searchForm.status" placeholder="请选择状态">
                            <el-option label="=-请选择-=" value=""></el-option>
                            <el-option label="上架" value="1"></el-option>
                            <el-option label="下架" value="0"></el-option>
                        </el-select>
                     </el-form-item>

                                                                           

                    <el-form-item>
                        <el-button type="success" @click="search()">查询<i class="el-icon-search el-icon--right"/></el-button>
                    </el-form-item>
                </el-row>
                <el-row class="ad" :style="{justifyContent:contents.btnAdAllBoxPosition=='1'?'flex-start':contents.btnAdAllBoxPosition=='2'?'center':'flex-end'}">
                    <el-form-item>
                        <el-button
                                v-if="isAuth('pet','新增')"
                                type="success"
                                icon="el-icon-plus"
                                @click="addOrUpdateHandler()"
                        >新增</el-button>
                        &nbsp;
                        <el-button
                                v-if="isAuth('pet','删除')"
                                :disabled="dataListSelections.length <= 0"
                                type="danger"
                                icon="el-icon-delete"
                                @click="deleteHandler()"
                        >删除</el-button>
                        &nbsp;
                    </el-form-item>
                </el-row>
            </el-form>
            <div class="table-content">
                <el-table class="tables" :size="contents.tableSize" :show-header="contents.tableShowHeader"
                          :header-row-style="headerRowStyle" :header-cell-style="headerCellStyle"
                          :border="contents.tableBorder"
                          :fit="contents.tableFit"
                          :stripe="contents.tableStripe"
                          :row-style="rowStyle"
                          :cell-style="cellStyle"
                          :style="{width: '100%',fontSize:contents.tableContentFontSize,color:contents.tableContentFontColor}"
                          v-if="isAuth('pet','查看')"
                          :data="dataList"
                          v-loading="dataListLoading"
                          @selection-change="selectionChangeHandler">
                    <el-table-column  v-if="contents.tableSelection"
                                      type="selection"
                                      header-align="center"
                                      align="center"
                                      width="50">
                    </el-table-column>
                    <el-table-column label="索引" v-if="contents.tableIndex" type="index" width="50" />

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="name"
                                   header-align="center"
                                   label="宠物名称">
                        <template slot-scope="scope">
                            {{scope.row.name}}
                        </template>
                    </el-table-column>

                    <el-table-column :sortable="contents.tableSortable" :align="contents.tableAlign" prop="imageUrl"
                                     header-align="center"
                                     width="150"
                                     label="宠物图片">
                        <template slot-scope="scope">
                            <div v-if="scope.row.imageUrl">
                                <img :src="scope.row.imageUrl" width="100" height="100">
                            </div>
                            <div v-else>无图片</div>
                        </template>
                    </el-table-column>

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="categoryName"
                                   header-align="center"
                                   label="分类">
                        <template slot-scope="scope">
                            {{scope.row.categoryName}}
                        </template>
                    </el-table-column>

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="gender"
                                   header-align="center"
                                   label="性别">
                        <template slot-scope="scope">
                            {{scope.row.gender}}
                        </template>
                    </el-table-column>

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="age"
                                   header-align="center"
                                   label="年龄">
                        <template slot-scope="scope">
                            {{scope.row.age}}
                        </template>
                    </el-table-column>

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="breed"
                                   header-align="center"
                                   label="品种">
                        <template slot-scope="scope">
                            {{scope.row.breed}}
                        </template>
                    </el-table-column>

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="price"
                                   header-align="center"
                                   label="价格">
                        <template slot-scope="scope">
                            {{scope.row.price}}
                        </template>
                    </el-table-column>

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="status"
                                   header-align="center"
                                   label="状态">
                        <template slot-scope="scope">
                            {{scope.row.status == 1 ? '上架' : '下架'}}
                        </template>
                    </el-table-column>

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="clickNum"
                                   header-align="center"
                                   label="点击数">
                        <template slot-scope="scope">
                            {{scope.row.clickNum}}
                        </template>
                    </el-table-column>

                    <el-table-column  :sortable="contents.tableSortable" :align="contents.tableAlign"
                                   prop="createTime"
                                   header-align="center"
                                   label="创建时间">
                        <template slot-scope="scope">
                            {{scope.row.createTime}}
                        </template>
                    </el-table-column>

                    <el-table-column width="250" :align="contents.tableAlign"
                                     header-align="center"
                                     label="操作">
                        <template slot-scope="scope">
                            <el-button v-if="isAuth('pet','查看')" type="success" icon="el-icon-tickets" size="mini" @click="addOrUpdateHandler(scope.row.id,'info')">详情</el-button>
                            <el-button v-if="isAuth('pet','修改')" type="primary" icon="el-icon-edit" size="mini" @click="addOrUpdateHandler(scope.row.id)">修改</el-button>
                            <el-button v-if="isAuth('pet','删除')" type="danger" icon="el-icon-delete" size="mini" @click="deleteHandler(scope.row.id)">删除</el-button>
                            <el-button v-if="isAuth('pet','修改')" type="primary" icon="el-icon-tickets" size="mini" @click="updateStatus(scope.row.id,scope.row.status)">{{scope.row.status == 1?'下架':'上架'}}</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-pagination
                        clsss="pages"
                        :layout="layouts"
                        @size-change="sizeChangeHandle"
                        @current-change="currentChangeHandle"
                        :current-page="pageIndex"
                        :page-sizes="[5, 10, 20, 50, 100]"
                        :page-size="Number(contents.pageEachNum)"
                        :total="totalPage"
                        :small="contents.pageStyle"
                        class="pagination-content"
                        :background="contents.pageBtnBG"
                        :style="{textAlign:contents.pagePosition==1?'left':contents.pagePosition==2?'center':'right'}"
                ></el-pagination>
            </div>
        </div>
        <!-- 添加/修改页面  将父组件的search方法传递给子组件-->
        <add-or-update v-if="addOrUpdateFlag" :parent="this" ref="addOrUpdate"></add-or-update>

    </div>
</template>
<script>
    import AddOrUpdate from "./add-or-update";
    import styleJs from "../../../utils/style.js";

    export default {
        data() {
        return {
            searchForm: {
                key: ""
            },
            sessionTable : "",//登录账户所在表名
            role : "",//权限
            userId:"",//当前登录人的id
    //级联表下拉框搜索条件
    //当前表下拉框搜索条件
              categorySelectSearch : [],
            form:{
                id : null,
                name : null,
                categoryId : null,
                gender : null,
                age : null,
                breed : null,
                price : null,
                description : null,
                imageUrl : null,
                status : null,
                clickNum : null,
                createTime : null,
            },
            dataList: [],
            pageIndex: 1,
            pageSize: 5,
            totalPage: 0,
            dataListLoading: false,
            dataListSelections: [],
            showFlag: true,
            addOrUpdateFlag:false,
            contents:null,
            layouts: '',

        };
        },
        created() {
            this.contents = styleJs.listStyle();
            this.init();
            this.getDataList();
            this.contentStyleChange()
        },
        mounted() {
            //获取当前登录用户的信息
            this.sessionTable = this.$storage.get("sessionTable");
            this.role = this.$storage.get("role");
            this.userId = this.$storage.get("userId");

        },
        filters: {
            htmlfilter: function (val) {
                return val.replace(/<[^>]*>/g).replace(/undefined/g,'');
            }
        },
        components: {
            AddOrUpdate,
        },
        methods: {
            contentStyleChange() {
                this.contentSearchStyleChange()
                this.contentBtnAdAllStyleChange()
                this.contentSearchBtnStyleChange()
                this.contentTableBtnStyleChange()
                this.contentPageStyleChange()
            },
            contentSearchStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.form-content .slt .el-input__inner').forEach(el => {
                        let textAlign = 'left'
                        if(this.contents.inputFontPosition == 2)
                            textAlign = 'center'
                            if (this.contents.inputFontPosition == 3) textAlign = 'right'
                                el.style.textAlign = textAlign
                            el.style.height = this.contents.inputHeight
                            el.style.lineHeight = this.contents.inputHeight
                            el.style.color = this.contents.inputFontColor
                            el.style.fontSize = this.contents.inputFontSize
                            el.style.borderWidth = this.contents.inputBorderWidth
                            el.style.borderStyle = this.contents.inputBorderStyle
                            el.style.borderColor = this.contents.inputBorderColor
                            el.style.borderRadius = this.contents.inputBorderRadius
                            el.style.backgroundColor = this.contents.inputBgColor
                    })
                    if (this.contents.inputTitle) {
                        document.querySelectorAll('.form-content .slt .el-form-item__label').forEach(el => {
                            el.style.color = this.contents.inputTitleColor
                            el.style.fontSize = this.contents.inputTitleSize
                            el.style.lineHeight = this.contents.inputHeight
                        })
                    }
                    setTimeout(() => {
                        document.querySelectorAll('.form-content .slt .el-input__prefix').forEach(el => {
                            el.style.color = this.contents.inputIconColor
                            el.style.lineHeight = this.contents.inputHeight
                        })
                        document.querySelectorAll('.form-content .slt .el-input__suffix').forEach(el => {
                            el.style.color = this.contents.inputIconColor
                            el.style.lineHeight = this.contents.inputHeight
                        })
                        document.querySelectorAll('.form-content .slt .el-input__icon').forEach(el => {
                            el.style.lineHeight = this.contents.inputHeight
                        })
                    }, 10 )
                })
            },
            // 搜索按钮
            contentSearchBtnStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.form-content .slt .el-button--success').forEach(el => {
                        el.style.height = this.contents.searchBtnHeight
                        el.style.color = this.contents.searchBtnFontColor
                        el.style.fontSize = this.contents.searchBtnFontSize
                        el.style.borderWidth = this.contents.searchBtnBorderWidth
                        el.style.borderStyle = this.contents.searchBtnBorderStyle
                        el.style.borderColor = this.contents.searchBtnBorderColor
                        el.style.borderRadius = this.contents.searchBtnBorderRadius
                        el.style.backgroundColor = this.contents.searchBtnBgColor
                    })
                })
            },
            // 新增、批量删除
            contentBtnAdAllStyleChange() {
                this.$nextTick(() => {
                    document.querySelectorAll('.form-content .ad .el-button--success').forEach(el => {
                        el.style.height = this.contents.btnAdAllHeight
                        el.style.color = this.contents.btnAdAllAddFontColor
                        el.style.fontSize = this.contents.btnAdAllFontSize
                        el.style.borderWidth = this.contents.btnAdAllBorderWidth
                        el.style.borderStyle = this.contents.btnAdAllBorderStyle
                        el.style.borderColor = this.contents.btnAdAllBorderColor
                        el.style.borderRadius = this.contents.btnAdAllBorderRadius
                        el.style.backgroundColor = this.contents.btnAdAllAddBgColor
                    })
                    document.querySelectorAll('.form-content .ad .el-button--danger').forEach(el => {
                        el.style.height = this.contents.btnAdAllHeight
                        el.style.color = this.contents.btnAdAllDelFontColor
                        el.style.fontSize = this.contents.btnAdAllFontSize
                        el.style.borderWidth = this.contents.btnAdAllBorderWidth
                        el.style.borderStyle = this.contents.btnAdAllBorderStyle
                        el.style.borderColor = this.contents.btnAdAllBorderColor
                        el.style.borderRadius = this.contents.btnAdAllBorderRadius
                        el.style.backgroundColor = this.contents.btnAdAllDelBgColor
                    })
                })
            },
            // 表格
            rowStyle({row, rowIndex}) {
                if (rowIndex % 2 == 1) {
                    if (this.contents.tableStripe) {
                        return {color: this.contents.tableStripeFontColor}
                    }
                } else {
                    return ''
                }
            },
            cellStyle({row, rowIndex}) {
                if (rowIndex % 2 == 1) {
                    if (this.contents.tableStripe) {
                        return {backgroundColor: this.contents.tableStripeBgColor}
                    }
                } else {
                    return ''
                }
            },
            headerRowStyle({row, rowIndex}) {
                return {color: this.contents.tableHeaderFontColor}
            },
            headerCellStyle({row, rowIndex}) {
                return {backgroundColor: this.contents.tableHeaderBgColor}
            },
            // 表格按钮
            contentTableBtnStyleChange() {
            },
            // 分页
            contentPageStyleChange() {
                let arr = []
                if (this.contents.pageTotal) arr.push('total')
                if (this.contents.pageSizes) arr.push('sizes')
                if (this.contents.pagePrevNext) {
                    arr.push('prev')
                    if (this.contents.pagePager) arr.push('pager')
                    arr.push('next')
                }
                if (this.contents.pageJumper) arr.push('jumper')
                this.layouts = arr.join()
                this.contents.pageEachNum = 5
            },

            init() {
            },
            search() {
                this.pageIndex = 1;
                this.getDataList();
            },
            // 获取数据列表
            getDataList() {
                this.dataListLoading = true;
                let params = {
                    page: this.pageIndex,
                    limit: this.pageSize,
                    sort: 'id',
                }

                 
                if (this.searchForm.name!= '' && this.searchForm.name!= undefined) {
                    params['name'] = '%' + this.searchForm.name + '%'
                }
                          
                if (this.searchForm.categoryId!= '' && this.searchForm.categoryId!= undefined) {
                    params['categoryId'] = this.searchForm.categoryId
                }
                          
                if (this.searchForm.status!= '' && this.searchForm.status!= undefined) {
                    params['status'] = this.searchForm.status
                }
                                                                          

                this.$http({
                    url: "pet/page",
                    method: "get",
                    params: params
                }).then(({data}) => {
                    if(data && data.code === 0){
                        this.dataList = data.data.list;
                        this.totalPage = data.data.total;
                    }else{
                        this.dataList = [];
                        this.totalPage = 0;
                    }
                    this.dataListLoading = false;
                });

                //查询分类用于下拉框
                this.$http({
                    url: "petCategory/list",
                    method: "get",
                }).then(({data}) => {
                    if(data && data.code === 0){
                        this.categorySelectSearch = data.data;
                    }
                });
            },
            //每页数
            sizeChangeHandle(val) {
                this.pageSize = val;
                this.pageIndex = 1;
                this.getDataList();
            },
            // 当前页
            currentChangeHandle(val) {
                this.pageIndex = val;
                this.getDataList();
            },
            // 多选
            selectionChangeHandler(val) {
                this.dataListSelections = val;
            },
            // 添加/修改
            addOrUpdateHandler(id, type) {
                this.showFlag = false;
                this.addOrUpdateFlag = true;
                if (type != 'info') {
                    type = 'else';
                }
                this.$nextTick(() => {
                    this.$refs.addOrUpdate.init(id, type);
                });
            },
            // 删除
            deleteHandler(id) {
                var ids = id ? [Number(id)] : this.dataListSelections.map(item => {
                    return Number(item.id);
                });

                this.$confirm(`确定进行[${id ? "删除" : "批量删除"}]操作?`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    this.$http({
                        url: "pet/delete",
                        method: "post",
                        data: ids
                    }).then(({data}) => {
                        if(data && data.code === 0){
                            this.$message({
                                message: "操作成功",
                                type: "success",
                                duration: 1500,
                                onClose: () => {
                                    this.search();
                                }
                            });
                        }else{
                            this.$message.error(data.msg);
                        }
                    });
                });
            },
            // 上架/下架
            updateStatus(id, status) {
                var msg = "";
                if(status == 1){
                    status = 0;
                    msg = "下架";
                }else if(status == 0){
                    status = 1;
                    msg = "上架";
                }
                var shuju = {
                        'id': id,
                        'status': status,
                    }
                this.$confirm(`确定要将它${msg}吗?`, "提示", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                   this.$http({
                        url: "pet/update",
                        method: "post",
                        data: shuju
                    }).then(({ data }) => {
                        if (data && data.code === 0) {
                            this.$message({
                                message: msg + "成功",
                                type: "success",
                                duration: 1500,
                                onClose: () => { this.search(); }
                            });
                        } else {
                            this.$message.error(data.msg);
                        }
                    });
                });
            },
        }
    };
</script>
<style lang="scss" scoped>
.slt {
    margin: 0 !important;
    display: flex;
  }

  .ad {
    margin: 0 !important;
    display: flex;
  }

  .pages {
    & /deep/ el-pagination__sizes{
      & /deep/ el-input__inner {
        height: 22px;
        line-height: 22px;
      }
    }
  }


  .el-button+.el-button {
    margin:0;
  }

  .tables {
	& /deep/ .el-button--success {
		height: 40px;
		color: var(--publicSubColor);
		font-size: 14px;
		border-width: 4px;
		border-style: double;
		border-color: var(--publicSubColor);
		border-radius: 0px;
		background-color: #fff;
	}

	& /deep/ .el-button--primary {
		height: 40px;
		color: var(--publicSubColor);
		font-size: 14px;
		border-width: 4px;
		border-style: double;
		border-color: var(--publicSubColor);
		border-radius: 0px;
		background-color: #fff;
	}

	& /deep/ .el-button--danger {
		height: 40px;
		color: var(--publicSubColor);
		font-size: 14px;
		border-width: 4px;
		border-style: double;
		border-color: var(--publicSubColor);
		border-radius: 0px;
		background-color: #fff;
	}

    & /deep/ .el-button {
      margin: 4px;
    }
  }
	.form-content {
		background: transparent;
	}
	.table-content {
</style>