import { defineStore } from 'pinia'

export const useUserInfoStore = defineStore('user-profile', {
  state: () => {
    return {
      user: {}
    }
  },
  actions: {
    setUser(user) {
      this.user = user
    },
    userIsEmpty () {
      return undefined === this.user || null === this.user || {} === this.user
    }
  }
})