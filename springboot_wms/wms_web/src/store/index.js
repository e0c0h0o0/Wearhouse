import { createStore } from 'vuex';
import router from "@/router";
function addNewRoute(menuList){
    let routes = router.options.routes;
    console.log(routes);
    routes.forEach(routeItem=>{
        if(routeItem.path=="Index"){
            menuList.forEach(menu=>{
                let childRoute={
                    path:'/'+menu.menuclick,
                    name:menu.menuname,
                    meta:{
                        title:"menu.menuname"
                    },
                    component:()=>import('../components/'+menu.menucomponent)
                }
                routeItem.children.push(childRoute)
            })
        }
    })

router.addRoute(routes)
}

const store = createStore({
    state() {
        return {
            menu: JSON.parse(sessionStorage.getItem('setMenu')) ? JSON.parse(sessionStorage.getItem('setMenu')) : []
        };
    },
    mutations: {
        setMenu(state, menuList) {
            state.menu = menuList;
            addNewRoute(menuList);
        }

    },
    getters: {
        getMenu(state) {
            return state.menu;
        }

    }
});

export default store;
