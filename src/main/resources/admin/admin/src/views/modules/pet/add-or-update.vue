<template>
    <div class="addOrUpdate">
        <el-dialog :title="title" :visible.sync="dialogFormVisible" :close-on-click-modal="false" width="800px">
            <el-form :model="form" label-position="right" label-width="120px">
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item label="宠物名称" prop="name" required>
                            <el-input v-model="form.name" placeholder="请输入宠物名称" maxlength="100" show-word-limit></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="分类" prop="categoryId" required>
                            <el-select v-model="form.categoryId" placeholder="请选择分类">
                                <el-option label="=-请选择-=" value=""></el-option>
                                <el-option
                                   v-for="(item,index) in categoryOptions"
                                   v-bind:key="index"
                                   :label="item.name"
                                   :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="性别" prop="gender" required>
                            <el-select v-model="form.gender" placeholder="请选择性别">
                                <el-option label="公" value="公"></el-option>
                                <el-option label="母" value="母"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="年龄" prop="age" required>
                            <el-input v-model="form.age" placeholder="请输入年龄，如：2个月" maxlength="20" show-word-limit></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="品种" prop="breed" required>
                            <el-input v-model="form.breed" placeholder="请输入品种" maxlength="100" show-word-limit></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="价格" prop="price" required>
                            <el-input v-model="form.price" type="number" placeholder="请输入价格" step="0.01" min="0"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="宠物图片" prop="imageUrl">
                            <el-upload
                                    class="upload-demo"
                                    :action="uploadUrl"
                                    :on-success="handleUploadSuccess"
                                    :on-error="handleUploadError"
                                    :limit="1"
                                    :file-list="fileList">
                                <el-button size="small" type="primary">点击上传</el-button>
                                <div slot="tip" class="el-upload__tip">只能上传一张图片</div>
                            </el-upload>
                            <div v-if="form.imageUrl" class="image-preview">
                                <img :src="form.imageUrl" width="200" height="200">
                            </div>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="状态" prop="status">
                            <el-select v-model="form.status" placeholder="请选择状态">
                                <el-option label="上架" value="1"></el-option>
                                <el-option label="下架" value="0"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item label="描述" prop="description">
                            <el-input v-model="form.description" type="textarea" placeholder="请输入描述" rows="4"></el-input>
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
                    categoryId: null,
                    gender: null,
                    age: null,
                    breed: null,
                    price: null,
                    description: null,
                    imageUrl: null,
                    status: 1,
                },
                categoryOptions: [],
                uploadUrl: '/wangshangchongwudian/file/upload',
                fileList: [],
            };
        },
        methods: {
            init(id, type) {
                this.form = {
                    id: null,
                    name: null,
                    categoryId: null,
                    gender: null,
                    age: null,
                    breed: null,
                    price: null,
                    description: null,
                    imageUrl: null,
                    status: 1,
                };
                this.fileList = [];
                this.dialogFormVisible = true;
                if (type === 'info') {
                    this.title = "详情";
                } else {
                    this.title = id ? "修改" : "新增";
                }
                if (id) {
                    this.$http({
                        url: "pet/info/" + id,
                        method: "get"
                    }).then(({data}) => {
                        if (data && data.code === 0) {
                            this.form = data.data;
                            if (this.form.imageUrl) {
                                this.fileList = [{url: this.form.imageUrl}];
                            }
                        }
                    });
                }
                // 查询分类
                this.$http({
                    url: "petCategory/list",
                    method: "get"
                }).then(({data}) => {
                    if (data && data.code === 0) {
                        this.categoryOptions = data.data;
                    }
                });
            },
            submitForm() {
                this.submitLoading = true;
                let url = this.form.id ? "pet/update" : "pet/save";
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
            handleUploadSuccess(response, file, fileList) {
                if (response.code === 0) {
                    this.form.imageUrl = response.file;
                    this.fileList = fileList;
                } else {
                    this.$message.error('上传失败');
                }
            },
            handleUploadError() {
                this.$message.error('上传失败');
            },
        }
    };
</script>

<style scoped>
    .addOrUpdate {
        margin-top: 20px;
    }
    .image-preview {
        margin-top: 10px;
    }
</style>