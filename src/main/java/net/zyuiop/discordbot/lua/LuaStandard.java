package net.zyuiop.discordbot.lua;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.BaseLib;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JseBaseLib;

/**
 * @author zyuiop
 */
public class LuaStandard extends JseBaseLib {
	private final Consumer<String> channel;
	Globals globals;

	public LuaStandard(Consumer<String> channel) {
		this.channel = channel;
	}

	@Override
	public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
		LuaValue ret = super.call(luaValue, luaValue1);
		luaValue1.set("print", new print());
		luaValue1.set("write", new write());

		try {
			Field glob = BaseLib.class.getDeclaredField("globals");
			glob.setAccessible(true);
			globals = (Globals) glob.get(this);
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}

		return ret;
	}

	final class print extends VarArgFunction {
		public Varargs invoke(Varargs var1) {
			LuaValue var2 = LuaStandard.this.globals.get("tostring");
			int var3 = 1;

			StringBuilder builder = new StringBuilder();
			for (int var4 = var1.narg(); var3 <= var4; ++var3) {
				if (var3 > 1) {
					builder.append('\t');
				}

				LuaString var5 = var2.call(var1.arg(var3)).strvalue();
				builder.append(var5.tojstring());
			}

			builder.append("\n");
			channel.accept(builder.toString());

			return NONE;
		}
	}

	final class write extends VarArgFunction {
		public Varargs invoke(Varargs var1) {
			LuaValue var2 = LuaStandard.this.globals.get("tostring");
			int var3 = 1;

			StringBuilder builder = new StringBuilder();
			for (int var4 = var1.narg(); var3 <= var4; ++var3) {
				if (var3 > 1) {
					builder.append('\t');
				}

				LuaString var5 = var2.call(var1.arg(var3)).strvalue();
				builder.append(var5.tojstring());
			}

			channel.accept(builder.toString());

			return NONE;
		}
	}
}
