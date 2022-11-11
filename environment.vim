" Startup File that's very specific to the environment and files that live
" within the project. It's not the best, but it gets the workspace into
" a place where it's ready to make sound, and it does so in a local and
" reproducable way.

"-------------------------------------------------------------------

" TODO: Ensure that the yaml file handles the relative path
let g:scnvim_sclang_options = ['-l', $YAML_CONFIG]
let g:scnvim_root_dir=$SCNVIM_TAGS_DIRECTORY

" Ex Commands to Start Environment After NVim Startup
function! LoadEnvironment(timer)
    :args ./src/client/supercollider_startup.scd

    :call tagbar#CloseWindow()

    " Initialize SuperCollider
    :b supercollider_startup.scd
    :0 " Go to the first line in the startup file (hacky)
    :call scnvim#sclang#open()
    :call scnvim#send_block()
    ":call scnvim#postwindow#close()

    :sleep 250m "Wait for flicker to finish

    " Run the Command to Organize the SuperCollider Status Windows
    :silent !./organize_windows.sh

    " Run the Command to Connect Baudline to Supercollider
    ":silent !./connect_baudline.sh

    " Generate tags for the environment
    :SCNvimTags

endfunction

" Function to Wait for NVim Startup
function! WaitForLoad()
    let timer = timer_start(50, 'LoadEnvironment')
endfunction

:call WaitForLoad()
