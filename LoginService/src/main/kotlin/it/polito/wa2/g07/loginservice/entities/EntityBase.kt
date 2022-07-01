package it.polito.wa2.g07.loginservice.entities

import org.springframework.data.util.ProxyUtils
import javax.persistence.MappedSuperclass
import java.io.Serializable
import javax.persistence.GeneratedValue
import javax.persistence.Id

@MappedSuperclass
abstract class EntityBase<T: Serializable>  {

    companion object{
        private const val serialVersionUID = -43869754L
    }

    @Id
    @GeneratedValue
    private var id:T? = null

    fun getId(): T? = id

    override fun equals(other: Any?): Boolean {
        if(other == null) return false
        if(other === this) return true
        if(javaClass != ProxyUtils.getUserClass(other)) return false
        other as EntityBase<*>
        return if (null == id) false else this.id == other.id
    }

    override fun hashCode(): Int {
        return 30
    }
}