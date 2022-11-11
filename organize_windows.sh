#! /bin/bash

# Wait for SuperCollider to create its windows
for ((i=1 ;i<=10;i++)); do
    WINDOW_ID="`icesh xembed | grep -i SuperCollider | grep "Node Tree" | awk '{print $1}'`"
    if [ -z "$WINDOW_ID" ]; then
        sleep 1;
    else
        break;
    fi
done

# SuperCollider State Windows
icesh -w `icesh xembed | grep -i SuperCollider | grep "Node Tree" | awk '{print $1}'` move 2100 0 sizeto 449 1355
icesh -w `icesh xembed | grep -i SuperCollider | grep "levels.*dBFS" | awk '{print $1}'` move 1536 765
icesh -w `icesh xembed | grep -i SuperCollider | grep "Freq Analyzer" | awk '{print $1}'` move 1484 1027
icesh -w `icesh xembed | grep -i SuperCollider | grep "localhost server" | awk '{print $1}'` move 1800 635

# QJackCtl State Windows
icesh -w `icesh xembed | grep "QjackCtl" | grep "Graph" | awk '{print $1}'` move 750 0 sizeto 1338 733
icesh -w `icesh xembed | grep "QjackCtl" | grep "JACK Audio Connection Kit" | awk '{print $1}'` move 273 503
icesh -w `icesh xembed | grep "QjackCtl" | grep "Status" | awk '{print $1}'` move 0 0  sizeto 738 471
