package ray.mintcat.attributeplusincreaser

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject
object AttributePlusIncreaser: Plugin(){

    @TInject(value = ["uuids.yml"], locale = "LOCALE-PRIORITY")
    lateinit var data: TConfig
        private set

}