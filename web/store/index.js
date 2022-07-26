import { defineStore } from 'pinia'

export const useUserInfoStore = defineStore('user-profile', {
  state: () => {
    return {
      userProfile: {}
    }
  },
  actions: {
    setUserProfile(userProfile) {
      this.userProfile = userProfile
    },
    userProfileIsEmpty () {
      return undefined === this.userProfile || null === this.userProfile || {} === this.userProfile
    }
  }
})