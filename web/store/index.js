import { defineStore } from 'pinia'

export const LoginDialogStore = defineStore('login-dialog', {
  state: () => {
    return {
      show: false
    }
  },
  actions: {
    open () {
      this.show = true
    },
    close () {
      this.show = false
    },
    changeShow (newShow) {
      this.show = newShow
    }
  }
})