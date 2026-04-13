<template>
    <div class="addOrUpdate">
        <el-dialog :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" width="800px">
            <el-form :model="form" label-position="right" label-width="120px">
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="分类名称" prop="name" required>
                            <el-input v-model="form.name" placeholder="请输入分类名称" maxlength="100" show-word-limit></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="父分类" prop="parentId">
                            <el-select v-model="form.parentId" placeholder="请选择父分类">
                                <el-option label="顶级分类" value="0"></el-option>
                                <el-option
                                   v-for="(item,index) in parentCategoryOptions"
                                   v-bind:key="index"
                                   :label="item.name"
                                   :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="排序" prop="sort">
                            <el-input-number v-model="form.sort" :min="0" :max="999" placeholder="请输入排序" style="width: 100%"></el-input-number>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取消</el-button>
                <el-button type="primary" @click="submitForm" :loading="submitLoading">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {validURL, validPhone, validEmail, validPostcode} from '../../../utils/validate';

    export default {
        data() {
            return {
                title: "",
                dialogFormVisible: true,
                submitLoading: false,
                form: {
                    id: null,
                    name: null,
                    parentId: 0,
                    sort: 0,
                },
                parentCategoryOptions: [],
            };
        },
        methods: {
            init(id, type) {
                this.form = {
                    id: null,
                    name: null,
                    parentId: 0,
                    sort: 0,
                };
                this.dialogFormVisible = true;
                if (type === 'info') {
                    this.title = "详情";
                } else {
                    this.title = id ? "修改" : "新增";
                }
                if (id) {
                    this.$http({
                        url: "petCategory/info/" + id,
                        method: "get"
                    }).then(({data}) => {
                        if (data && data.code === 0) {
                            this.form = data.data;
                        }
                    });
                }
                // 查询顶级分类
                this.$http({
                    url: "petCategory/topLevel",
                    method: "get"
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.parentCategoryOptions = data.data;
                    }
                });
            },
            submitForm() {
                this.submitLoading = true;
                let url = this.form.id ? "petCategory/update" : "petCategory/save";
                this.$http({
                    url: url,
                    method: "post",
                    data: this.form
                }).then(({data}) => {
                    this.submitLoading = false;
                    if (data && data.code === 0) {
                        this.$message({
                            message: "操作成功",
                            type: "success",
                            duration: 1500,
                            onClose: () => {
                                this.dialogFormVisible = false;
                                this.$emit('refreshDataList');
                                this.parent.search();
                            }
                        });
                    } else {
                        this.$message.error(data.msg);
                    }
                });
            },
        }
    };
</script>

<style scoped>
    .addOrUpdate {
        margin-top: 20px;
    }
</style>