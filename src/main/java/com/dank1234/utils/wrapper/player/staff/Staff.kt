package com.dank1234.utils.wrapper.player.staff;

import com.dank1234.plugin.Main;
import com.dank1234.utils.RankUtils;
import com.dank1234.utils.data.database.StaffManager;
import com.dank1234.utils.wrapper.player.User;
import org.bukkit.Bukkit;

import javax.annotation.Nullable;
import java.util.UUID;

data class Staff(
    override var uuid: UUID,
    override var username: String,
    private var rank: StaffRank,
    private var time: Long = 0,
    private var messages: Int = 0,
    private var warns: Int = 0,
    private var mutes: Int = 0,
    private var bans: Int = 0,
    private var staffMode: Boolean = false
) : User(uuid, username) {

    companion object {
        fun of(uuid: UUID, username: String, rank: StaffRank): Staff {
            return Staff(uuid, username, rank)
        }
        fun of(uuid: UUID): Staff? {
        return StaffManager.getStaff(uuid).orElse(null)
        }

        fun of(username: String): Staff? {
        return StaffManager.getStaff(username).orElse(null)
        }
    }

    fun rank(): StaffRank = rank

    fun setRank(rank: StaffRank): Staff {
        this.rank = rank
        StaffManager.setValue(super.uuid, "rank", rank.toString())
        return this
    }

    fun time(): Long = time

    fun setTime(time: Long): Staff {
        this.time = time
        StaffManager.setValue(super.uuid, "time", time)
        return this
    }

    fun messages(): Int = messages

    fun setMessages(messages: Int): Staff {
        this.messages = messages
        StaffManager.setValue(super.uuid, "messages", messages)
        return this
    }

    fun warns(): Int = warns

    fun setWarns(warns: Int): Staff {
        this.warns = warns
        StaffManager.setValue(super.uuid, "warns", warns)
        return this
    }

    fun mutes(): Int = mutes

    fun setMutes(mutes: Int): Staff {
        this.mutes = mutes
        StaffManager.setValue(super.uuid, "mutes", mutes)
        return this
    }

    fun bans(): Int = bans

    fun setBans(bans: Int): Staff {
        this.bans = bans
        StaffManager.setValue(super.uuid, "bans", bans)
        return this
    }

    fun staffMode(): Boolean = staffMode

    fun setStaffMode(staffMode: Boolean): Staff {
        if (this.staffMode == staffMode) return this
        this.staffMode = staffMode
        StaffManager.setValue(super.uuid, "staffmode", staffMode)

        Bukkit.getScheduler().runTask(Main.get(), Runnable {
            if (staffMode) {
                RankUtils.removeStaffTrack(User.of(super.uuid))
            } else {
                val username = User.of(super.uuid)?.username
                if (username != null) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user $username parent add ${rank.rank.name}")
                }
            }
        })
        return this
    }

    override fun toString(): String {
        return """
            Staff[
                name: $username
                uuid: $uuid
                rank: $rank
                time: $time
                messages: $messages
                warns: $warns
                mutes: $mutes
                bans: $bans
            ]
        """.trimIndent() // test
    }
}