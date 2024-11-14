
setblock ~ ~ ~ chiseled_bookshelf
summon marker ~ ~ ~ {Tags:["shelf_loot"]}
function example:loops/10s
execute as @e[type=marker,tag=shelf_loot] at @s run function example:shelf_loot
schedule function example:loops/10s 10s
loot insert ~ ~ ~ loot loot_n_explore:bookshelf/testbook
kill @s