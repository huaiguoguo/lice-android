/**
 * Created by ice1000 on 2017/2/17.
 *
 * @author ice1000
 * @since 1.0.0
 */
package org.lice.core

import org.lice.compiler.model.EmptyNode
import org.lice.compiler.model.MetaData
import org.lice.compiler.model.Node
import org.lice.compiler.model.ValueNode
import java.util.*

@SinceKotlin("1.1")
typealias Func = (MetaData, List<Node>) -> Node

@SinceKotlin("1.1")
typealias ProvidedFuncWithMeta = (MetaData, List<Any?>) -> Any?

@SinceKotlin("1.1")
typealias ProvidedFunc = (List<Any?>) -> Any?

operator fun Func.invoke(e: MetaData) = invoke(e, emptyList())

class SymbolList
@JvmOverloads
constructor(init: Boolean = true) {
	companion object ClassPathHolder {
		val pathSeperator = System.getProperty("path.separator")
		val classPath = System.getProperty("java.class.path")
	}

	val functions = mutableMapOf<String, Func>()

	val rand = Random(System.currentTimeMillis())
//	val loadedModules = mutableListOf<String>()

	init {
		if (init) {
//			initialize()
//			loadLibrary()
		}
	}

	fun initialize() {
	}

	fun provideFunctionWithMeta(name: String, node: ProvidedFuncWithMeta) =
			defineFunction(name, { meta, ls ->
				val value = node(meta, ls.map { it.eval().o })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			})

	fun provideFunction(name: String, node: ProvidedFunc) =
			defineFunction(name, { meta, ls ->
				val value = node(ls.map { it.eval().o })
				if (value != null) ValueNode(value, meta)
				else EmptyNode(meta)
			})

	fun defineFunction(name: String, node: Func) =
			functions.put(name, node)

	fun isFunctionDefined(name: String?) =
			functions.containsKey(name)

	fun removeFunction(name: String?) =
			functions.remove(name)

	fun getFunction(name: String?) =
			functions[name]
}
