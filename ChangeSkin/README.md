# 换肤方案
#### CustomPaint目录
自定义ViewGroup，利用Canvas + 自定义Paint，完成变灰需求

#### Part目录
单独对color、text、Drawable、Bitmap进行变灰处理。当然，还有一个终极方法——硬件加速，但是不建议使用。

#### ReplaceAttr & ReplaceView 目录
参考AppCompactActivity替换View的思路，自定义LayoutInflater.Factory2处理每一个View（或者你可以直接重写onCreateView）。