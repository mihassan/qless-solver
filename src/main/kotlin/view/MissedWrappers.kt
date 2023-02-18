package view

import mui.material.*

inline var TextFieldProps.InputProps: InputBaseProps
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic().InputProps = value
    }
