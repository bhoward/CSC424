function concatenate(a1, a2)
   local result = {}
   for _, v in pairs(a1) do
      result[#result + 1] = v
   end
   for _, v in pairs(a2) do
      result[#result + 1] = v
   end
   return result
end
