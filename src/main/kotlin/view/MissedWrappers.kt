package view

import mui.material.*

inline var BoxProps.gap: Int
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic().gap = value
    }

inline var TextFieldProps.InputProps: InputBaseProps
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic().InputProps = value
    }
