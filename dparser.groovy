import java.util.regex.*;

println args[0]

// NNdDD+MMxTTTT;GGGG
// 10d12+20x1000;2000

def pt = Pattern.compile(/([0-9]*)d([0-9]*)[+]?([0-9]*)[x]?([0-9]*)[;]?([0-9]*)/)
def matcher = pt.matcher(args[0])

println "Matches: ${matcher.matches()}"

matcher.groupCount().times { g->
    println "Group[$g]: ${matcher.group(g)}"
}

println '--'
println "#:    ${matcher.group(1)}"
println "D:    ${matcher.group(2)}"
println "Mod:  ${matcher.group(3)}"
println "Mult: ${matcher.group(4)}"
println "Base: ${matcher.group(5)}"
