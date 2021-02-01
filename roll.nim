from os import paramStr, paramCount
import strutils, random
import nre except toSeq

randomize()

proc captured(cap: string, orUse: int): int =
  if cap != "": cap.parseInt()
  else: orUse

proc roll(n: int, d: int, m: int): (int, seq[int]) =
  var rolls: seq[int] = @[]
  var rollTotal: int = m

  for r in 0..n:
    let roll = rand(1..d)
    rollTotal += roll
    rolls.add(roll)

  (rollTotal, rolls)


let regex = "([0-9]*)d([0-9]*)[+]?([0-9]*)".re

let match = paramStr(1).match(regex).get
var numRolls = 1
if paramCount() > 1:
  numRolls = paramStr(2).parseInt()

let count = captured(match.captures[0], 1)
let die = match.captures[1].parseInt()
let modifier = captured(match.captures[2], 0)

echo "Rolling ", count, "d", die, "+", modifier, " ", numRolls, " time(s):"

for r in 1..numRolls:
  let rolled = roll(count, die, modifier)
  echo "[", r, "] (", rolled[1].join(", "), "): ", rolled[0]
